package com.group18.ideohub.service.boards;

import com.group18.ideohub.dto.BoardsCommentDTO;
import com.group18.ideohub.dto.BoardsDTO;
import com.group18.ideohub.dto.BoardsRespDTO;
import com.group18.ideohub.exception.BadRequestException;
import com.group18.ideohub.exception.ResourceNotFoundException;
import com.group18.ideohub.model.boards.BoardsComment;
import com.group18.ideohub.model.boards.BoardsModel;
import com.group18.ideohub.repo.boards.BoardRepo;
import com.group18.ideohub.service.UserService;
import com.group18.ideohub.service.cloudinary.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepo repository;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;

    public List<BoardsModel> getAllBoards() {
        String userId = userService.getCurrentUser();
        List<BoardsModel> userBoards = repository.findByUserId(userId);
        List<BoardsModel> accessibleBoards = new java.util.ArrayList<>(userBoards);

        List<BoardsModel> allBoards = repository.findAll();
        for (BoardsModel board : allBoards) {
            if (!board.getUserId().equals(userId) && board.getAllowedUsers() != null
                    && board.getAllowedUsers().contains(userId)) {
                accessibleBoards.add(board);
            }
        }
        return accessibleBoards;
    }

    public BoardsModel createBoard(BoardsDTO board, MultipartFile image) {
        String userId = userService.getCurrentUser();
        String imageUrl = null;

        if (image != null && !image.isEmpty()) {
            imageUrl = cloudinaryService.uploadAndReturnUrl(image);
        }

        int boardNumber;
        do {
            boardNumber = (int) (Math.random() * 1000000);
        } while (repository.existsByBoardNumber(boardNumber));

        BoardsModel boardsModel = BoardsModel.builder()
                .boardId(UUID.randomUUID().toString())
                .UserId(userId)
                .Title(board.getTitle())
                .description(board.getDescription())
                .layout(board.getLayout())
                .isPublic(board.isPublic())
                .createdAt(String.valueOf(System.currentTimeMillis()))
                .updatedAt(String.valueOf(System.currentTimeMillis()))
                .imageUrl(imageUrl)
                .boardNumber(boardNumber)
                .build();
        return repository.save(boardsModel);
    }

    public BoardsModel updateBoard(String id, BoardsDTO board) {
        BoardsModel boardsModel = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + id));
        boardsModel.setTitle(board.getTitle());
        boardsModel.setDescription(board.getDescription());
        boardsModel.setLayout(board.getLayout());
        boardsModel.setPublic(board.isPublic());
        boardsModel.setUpdatedAt(String.valueOf(System.currentTimeMillis()));
        return repository.save(boardsModel);
    }

    public void deleteBoard(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Board not found with id: " + id);
        }
        repository.deleteById(id);
    }

    public BoardsModel getBoardById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Board not found with id: " + id));
    }

    public void addCommentToBoard(BoardsCommentDTO entity, String id, MultipartFile image) {
        BoardsModel boardsModel = getBoardById(id);

        String imageUrl = null;
        String link = null;

        if (image != null && !image.isEmpty()) {
            imageUrl = cloudinaryService.uploadAndReturnUrl(image);
        } else if (entity.getLinkUrl() != null && !entity.getLinkUrl().isEmpty()) {
            link = entity.getLinkUrl();
        }

        BoardsComment comment = BoardsComment.builder()
                .commentId(UUID.randomUUID().toString())
                .userCommentId(userService.getCurrentUser())
                .commentText(entity.getCommentText())
                .imageUrl(imageUrl)
                .LinkUrl(link)
                .createdAt(String.valueOf(System.currentTimeMillis()))
                .updatedAt(String.valueOf(System.currentTimeMillis()))
                .build();

        if (boardsModel.getComments() == null) {
            boardsModel.setComments(new java.util.ArrayList<>());
        }
        boardsModel.getComments().add(comment);
        repository.save(boardsModel);
    }

    public void deleteComment(String commentId, String boardId) {
        BoardsModel boardsModel = getBoardById(boardId);
        String currentUserId = userService.getCurrentUser();
        boolean removed = boardsModel.getComments() != null &&
                boardsModel.getComments().removeIf(comment -> comment.getCommentId().equals(commentId) &&
                        comment.getUserCommentId().equals(currentUserId));
        if (!removed) {
            throw new BadRequestException("Comment not found or user not authorized.");
        }
        repository.save(boardsModel);
    }

    public List<BoardsRespDTO> getAllPublicBoards() {
        List<BoardsModel> allBoards = repository.findAll();
        List<BoardsRespDTO> publicBoards = new java.util.ArrayList<>();
        for (BoardsModel board : allBoards) {
            if (board.isPublic()) {
                BoardsRespDTO dto = new BoardsRespDTO();
                dto.setBoardId(board.getBoardId());
                dto.setTitle(board.getTitle());
                dto.setDescription(board.getDescription());
                dto.setLayout(board.getLayout());
                dto.setPublic(board.isPublic());
                dto.setImageUrl(board.getImageUrl());
                publicBoards.add(dto);
            }
        }
        return publicBoards;
    }

    public BoardsModel getBoardByJoinCode(String code) {
        int boardNumber;
        try {
            boardNumber = Integer.parseInt(code);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid join code format.");
        }

        BoardsModel board = repository.findByBoardNumber(boardNumber);
        if (board == null) {
            throw new ResourceNotFoundException("Board not found with join code: " + code);
        }

        String userId = userService.getCurrentUser();
        ArrayList<String> allowedUsers = board.getAllowedUsers();
        if (allowedUsers == null) {
            allowedUsers = new java.util.ArrayList<>();
        }

        if (!allowedUsers.contains(userId)) {
            allowedUsers.add(userId);
            board.setAllowedUsers(allowedUsers);
            repository.save(board);
        }

        return board;
    }

    public BoardsModel getBoardByJoinWithLink(String boardId) {
        BoardsModel board = getBoardById(boardId);
        String userId = userService.getCurrentUser();
        ArrayList<String> allowedUsers = board.getAllowedUsers();
        if (allowedUsers == null) {
            allowedUsers = new ArrayList<>();
        }

        if (!allowedUsers.contains(userId)) {
            allowedUsers.add(userId);
            board.setAllowedUsers(allowedUsers);
            repository.save(board);
        }

        return board;
    }
}
