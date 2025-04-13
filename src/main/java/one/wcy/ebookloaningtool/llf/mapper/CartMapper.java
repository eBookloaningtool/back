package one.wcy.ebookloaningtool.llf.mapper;


import one.wcy.ebookloaningtool.llf.pojo.Cart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CartMapper {

    //根据userUUID查找所有的bookId
    @Select("select bookId from Cart where userUUID = #{id}")
    List<String> findListByUid(String id);

    //根据BookId和userUUID查找Cart
    @Select("select * from Cart where userUUID = #{uid} and bookId = #{bid}")
    Cart findListByUidAndBookId(String uid, String bid);

    //根据提供的cart实体插入
    @Insert("insert into Cart(userUUID, bookId, addedAt) values(#{uid}, #{bid}, now())")
    void addCart(String uid, String bid);

    //根据提供的userUUID和bookId删除
    @Delete("delete from Cart where userUUID = #{uid} and bookId = #{bid}")
    void deleteCart(String uid, String bid);

}
