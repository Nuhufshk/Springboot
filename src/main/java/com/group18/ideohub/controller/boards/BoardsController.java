package com.group18.ideohub.controller.boards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.group18.ideohub.dto.BoardsCommentDTO;
import com.group18.ideohub.dto.BoardsDTO;
import com.group18.ideohub.response.RegisterResponse;
import com.group18.ideohub.service.boards.BoardService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@CrossOrigin
@RequestMapping("/api/boards")
public class BoardsController {

    @Autowired
    private BoardService boardsService;

    @GetMapping
    public ResponseEntity<RegisterResponse<?>> getMethodName() {
        RegisterResponse<?> response;
        try {
            response = new RegisterResponse<>(true, "Success", boardsService.getAllBoards());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response = new RegisterResponse<>(false, "An error occurred: " + e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping
    public ResponseEntity<RegisterResponse<?>> createBoards(@RequestPart("board") BoardsDTO board,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        RegisterResponse<?> response;
        try {
            boardsService.createBoard(board, image);
            response = new RegisterResponse<>(true, "Board created successfully", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response = new RegisterResponse<>(false, "An error occurred: " + e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<RegisterResponse<?>> deleteBoard(@PathVariable String boardId) {
        RegisterResponse<?> response;
        try {
            boardsService.deleteBoard(boardId);
            response = new RegisterResponse<>(true, "Board deleted successfully", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response = new RegisterResponse<>(false, "An error occurred: " + e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegisterResponse<?>> updateBoard(@PathVariable String id, @RequestBody BoardsDTO board) {
        RegisterResponse<?> response;
        try {
            boardsService.updateBoard(id, board);
            response = new RegisterResponse<>(true, "Board updated successfully", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response = new RegisterResponse<>(false, "An error occurred: " + e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegisterResponse<?>> getBoardById(@PathVariable String id) {
        RegisterResponse<?> response;
        try {
            response = new RegisterResponse<>(true, "Success", boardsService.getBoardById(id));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response = new RegisterResponse<>(false, "An error occurred: " + e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/comments/{id}")
    public ResponseEntity<RegisterResponse<?>> postMethodName(@RequestBody BoardsCommentDTO entity,
            @PathVariable String id, @RequestPart(value = "image", required = false) MultipartFile image) {
        try {
            boardsService.addCommentToBoard(entity, id, image);
            RegisterResponse<?> response = new RegisterResponse<>(true, "Comment added successfully", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            RegisterResponse<?> response = new RegisterResponse<>(false, "An error occurred: " + e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/comments/{boardId}/{commentId}")
    public ResponseEntity<RegisterResponse<?>> deleteComment(@PathVariable String boardId,
            @PathVariable String commentId) {
        try {
            boardsService.deleteComment(commentId, boardId);
            RegisterResponse<?> response = new RegisterResponse<>(true, "Comment deleted successfully", null);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            RegisterResponse<?> response = new RegisterResponse<>(false, "An error occurred: " + e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/public/boards")
    public ResponseEntity<RegisterResponse<?>> getAllPublicBoards() {
        RegisterResponse<?> response;
        try {
            response = new RegisterResponse<>(true, "Success", boardsService.getAllPublicBoards());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response = new RegisterResponse<>(false, "An error occurred: " + e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }

}
