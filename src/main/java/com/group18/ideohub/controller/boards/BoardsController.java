package com.group18.ideohub.controller.boards;

import com.group18.ideohub.dto.BoardsCommentDTO;
import com.group18.ideohub.dto.BoardsDTO;
import com.group18.ideohub.model.boards.BoardsModel;
import com.group18.ideohub.response.RegisterResponse;
import com.group18.ideohub.service.boards.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardsController {

    private final BoardService boardsService;

    @Operation(summary = "Get all boards for the current user")
    @GetMapping
    public ResponseEntity<RegisterResponse<List<BoardsModel>>> getAllBoards() {
        return ResponseEntity.ok(new RegisterResponse<>(true, "Success", boardsService.getAllBoards()));
    }

    @Operation(summary = "Create a new board")
    @PostMapping
    public ResponseEntity<RegisterResponse<BoardsModel>> createBoard(
            @RequestBody MultipartFile image) {
        BoardsModel createdBoard = boardsService.createBoard(image);
        return ResponseEntity.ok(new RegisterResponse<>(true, "Board created successfully", createdBoard));
    }

    @Operation(summary = "Create a new board")
    @PostMapping("/details")
    public ResponseEntity<RegisterResponse<BoardsModel>> createBoardDetails(@RequestBody BoardsDTO board) {
        BoardsModel createdBoard = boardsService.createBoardDetails(board);
        return ResponseEntity.ok(new RegisterResponse<>(true, "Board created successfully", createdBoard));
    }

    @Operation(summary = "Create a new board")
    @PostMapping("/2nd-edition")
    public ResponseEntity<RegisterResponse<BoardsModel>> createBoards(@RequestPart("boards") String board,
            @RequestPart("image") MultipartFile image) {
        BoardsModel newBoard = new ObjectMapper().readValue(board, BoardsDTO.class);
        BoardsModel createdBoard = boardsService.createBoardDetails2n(newBoard, image);
        return ResponseEntity.ok(new RegisterResponse<>(true, "Board created successfully", createdBoard));
    }

    @Operation(summary = "Delete a board by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Board deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Board not found") })
    @DeleteMapping("/{boardId}")
    public ResponseEntity<RegisterResponse<Void>> deleteBoard(@PathVariable String boardId) {
        boardsService.deleteBoard(boardId);
        return ResponseEntity.ok(new RegisterResponse<>(true, "Board deleted successfully", null));
    }

    @Operation(summary = "Update a board by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Board updated successfully"),
            @ApiResponse(responseCode = "404", description = "Board not found") })
    @PutMapping("/{id}")
    public ResponseEntity<RegisterResponse<BoardsModel>> updateBoard(@PathVariable String id,
            @RequestBody BoardsDTO board) {
        BoardsModel updatedBoard = boardsService.updateBoard(id, board);
        return ResponseEntity.ok(new RegisterResponse<>(true, "Board updated successfully", updatedBoard));
    }

    @Operation(summary = "Get a board by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Board not found") })
    @GetMapping("/{id}")
    public ResponseEntity<RegisterResponse<BoardsModel>> getBoardById(@PathVariable String id) {
        return ResponseEntity.ok(new RegisterResponse<>(true, "Success", boardsService.getBoardById(id)));
    }

    @Operation(summary = "Add a comment to a board")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment added successfully"),
            @ApiResponse(responseCode = "404", description = "Board not found") })
    @PostMapping("/comments/{id}")
    public ResponseEntity<RegisterResponse<Void>> addComment(@RequestBody BoardsCommentDTO entity,
            @PathVariable String id, @RequestPart(value = "image", required = false) MultipartFile image) {
        boardsService.addCommentToBoard(entity, id, image);
        return ResponseEntity.ok(new RegisterResponse<>(true, "Comment added successfully", null));
    }

    @Operation(summary = "Delete a comment from a board")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Board or comment not found") })
    @DeleteMapping("/comments/{boardId}/{commentId}")
    public ResponseEntity<RegisterResponse<Void>> deleteComment(@PathVariable String boardId,
            @PathVariable String commentId) {
        boardsService.deleteComment(commentId, boardId);
        return ResponseEntity.ok(new RegisterResponse<>(true, "Comment deleted successfully", null));
    }

    @Operation(summary = "Get all public boards")
    @GetMapping("/public/boards")
    public ResponseEntity<RegisterResponse<?>> getAllPublicBoards() {
        return ResponseEntity.ok(new RegisterResponse<>(true, "Success", boardsService.getAllPublicBoards()));
    }

    @Operation(summary = "Join a board with a code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Board not found"),
            @ApiResponse(responseCode = "400", description = "Invalid join code format") })
    @GetMapping("/join/code/{code}")
    public ResponseEntity<RegisterResponse<BoardsModel>> joinWithCode(@PathVariable String code) {
        return ResponseEntity.ok(new RegisterResponse<>(true, "Success", boardsService.getBoardByJoinCode(code)));
    }

    @Operation(summary = "Join a board with a link")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "404", description = "Board not found") })
    @GetMapping("/join/link/{boardId}")
    public ResponseEntity<RegisterResponse<BoardsModel>> joinWithLink(@PathVariable String boardId) {
        return ResponseEntity
                .ok(new RegisterResponse<>(true, "Success", boardsService.getBoardByJoinWithLink(boardId)));
    }
}
