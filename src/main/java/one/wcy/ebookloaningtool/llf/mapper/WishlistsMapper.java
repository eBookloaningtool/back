package one.wcy.ebookloaningtool.llf.mapper;


import one.wcy.ebookloaningtool.llf.pojo.Wishlists;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface WishlistsMapper {

    //根据userUUID查找所有的bookId
    @Select("select bookId from Wishlists where userUUID = #{id}")
    List<String> findListByUid(String id);

    //根据BookId查找wishlist
    @Select("select * from Wishlists where userUUID = #{uid} and bookId = #{bid}")
    Wishlists findListByUidAndBookId(String uid, String bid);

    //根据提供的wishlists实体插入
    @Insert("insert into Wishlists(userUUID, bookId, addedAt) values(#{uid}, #{bid}, now())")
    void addWishlists(String uid, String bid);

    //根据提供的userUUID和bookId删除
    @Delete("delete from Wishlists where userUUID = #{uid} and bookId = #{bid}")
    void deleteWishlists(String uid, String bid);

}
