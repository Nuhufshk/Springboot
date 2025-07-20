package com.group18.ideohub.service.boards;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.group18.ideohub.dto.BoardsRespDTO;

import com.group18.ideohub.dto.BoardsCommentDTO;
import com.group18.ideohub.dto.BoardsDTO;
import com.group18.ideohub.model.boards.BoardsComment;
import com.group18.ideohub.model.boards.BoardsModel;
import com.group18.ideohub.repo.boards.BoardRepo;
import com.group18.ideohub.service.UserService;
import com.group18.ideohub.service.cloudinary.CloudinaryService;

@Service
public class BoardService {

    @Autowired
    private BoardRepo repository;

    @Autowired
    private UserService userService;

    @Autowired
    private CloudinaryService cloudinaryService;

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

    public void createBoard(BoardsDTO board, MultipartFile image) {
        String userId = userService.getCurrentUser();
        String imageUrl = null;

        // check if the image is not null and handle it accordingly
        if (image != null && !image.isEmpty()) {
            imageUrl = cloudinaryService.uploadAndReturnUrl(image);
        }

        // Generate a unique random boardNumber
        int boardNumber;
        do {
            boardNumber = (int) (Math.random() * 1000000); // 6-digit random number
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
                .comments(null)
                .boardNumber(boardNumber)
                .build();
        repository.save(boardsModel);
    }

    public void updateBoard(String id, BoardsDTO board) {
        BoardsModel boardsModel = repository.findById(id).orElse(null);
        if (boardsModel != null) {
            boardsModel.setTitle(board.getTitle());
            boardsModel.setDescription(board.getDescription());
            boardsModel.setLayout(board.getLayout());
            boardsModel.setPublic(board.isPublic());
            boardsModel.setUpdatedAt(String.valueOf(System.currentTimeMillis()));
            repository.save(boardsModel);
        }
    }

    public void deleteBoard(String id) {
        repository.deleteById(id);
    }

    public Object getBoardById(String id) {
        BoardsModel boardsModel = repository.findById(id).orElse(null);
        if (boardsModel != null) {
            return boardsModel;
        } else {
            throw new RuntimeException("Board not found with id: " + id);
        }
    }

    public void addCommentToBoard(BoardsCommentDTO entity, String id, MultipartFile image) {
        BoardsModel boardsModel = repository.findById(id).orElse(null);

        if (boardsModel != null) {
            String imageUrl = null;
            String link = null;

            // Check if image is present and not empty
            if (image != null && !image.isEmpty()) {
                imageUrl = cloudinaryService.uploadAndReturnUrl(image);
            } else if (entity.getLinkUrl() != null && !entity.getLinkUrl().isEmpty()) {
                link = entity.getLinkUrl();
            }

            BoardsComment comment = BoardsComment.builder()
                    .commentId(UUID.randomUUID().toString())
                    .userId(userService.getCurrentUser())
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
    }

    public void deleteComment(String commentId, String boardId) {
        BoardsModel boardsModel = repository.findById(boardId).orElse(null);
        if (boardsModel != null) {
            String currentUserId = userService.getCurrentUser();
            boolean removed = boardsModel.getComments() != null &&
                    boardsModel.getComments().removeIf(comment -> comment.getCommentId().equals(commentId) &&
                            comment.getUserId().equals(currentUserId));
            if (removed) {
                repository.save(boardsModel);
            } else {
                throw new RuntimeException("Comment not found or user not authorized.");
            }
        } else {
            throw new RuntimeException("Board not found with id: " + boardId);
        }
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

    public Object getBoardByJoinCode(String code) {
        int boardNumber;
        try {
            boardNumber = Integer.parseInt(code);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid join code format.");
        }

        BoardsModel board = repository.findByBoardNumber(boardNumber);
        if (board == null) {
            throw new RuntimeException("Board not found with join code: " + code);
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

    public Object getBoardByJoinWithLink(String boardId) {
        BoardsModel board = repository.findById(boardId).orElse(null);
        if (board == null) {
            throw new RuntimeException("Board not found with id: " + boardId);
        }

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
