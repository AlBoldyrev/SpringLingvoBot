package com.vk.repository;

import com.vk.entities.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StorageRepository extends JpaRepository<Storage, Integer> {

    Storage findByStorageId(Integer id);

    @Query("SELECT s FROM Storage s WHERE s.userVkId = :vkId")
    List<Storage> findByUserVkId(@Param("vkId") Integer vkId);
}
