package one.wcy.ebookloaningtool.llf.service;

import one.wcy.ebookloaningtool.llf.pojo.BorrowRecords;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRecordsRepository extends JpaRepository<BorrowRecords, String> {

    //根据userid,bookid,status查找记录,可能存在多条，用List存储
    List<BorrowRecords> findByBookUUIDAndUserUUIDAndStatus(String bookUUID, String userUUID, String status);
    //根据status查找记录，可能存在多条，用List存储
    List<BorrowRecords> findByStatus(String status);
}
