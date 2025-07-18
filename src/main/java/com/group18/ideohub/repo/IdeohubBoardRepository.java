package com.group18.ideohub.repo;

import com.group18.ideohub.model.IdeohubBoard;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface IdeohubBoardRepository extends MongoRepository<IdeohubBoard, String> {
}
