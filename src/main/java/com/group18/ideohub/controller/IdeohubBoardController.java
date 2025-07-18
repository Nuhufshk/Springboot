package com.group18.ideohub.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.group18.ideohub.model.IdeohubBoard;
import com.group18.ideohub.repo.IdeohubBoardRepository;

@RestController
@RequestMapping("/api/boards")
public class IdeohubBoardController {

    @Autowired
    private IdeohubBoardRepository IdeohubBoardRepository;

    @GetMapping
    public List<IdeohubBoard> getAllBoards() {
        return IdeohubBoardRepository.findAll();
    }

    @PostMapping
    public IdeohubBoard createBoard(@RequestBody IdeohubBoard board) {
        return IdeohubBoardRepository.save(board);
    }

    @GetMapping("/{id}")
    public IdeohubBoard getBoardById(@PathVariable Long id) {
        return IdeohubBoardRepository.findById(id).orElse(null);
    }

    @PutMapping("/{id}")
    public IdeohubBoard updateBoard(@PathVariable Long id, @RequestBody IdeohubBoard board) {
        board.setId(id);
        return IdeohubBoardRepository.save(board);
    }

    @DeleteMapping("/{id}")
    public void deleteBoard(@PathVariable Long id) {
        IdeohubBoardRepository.deleteById(id);
    }
}