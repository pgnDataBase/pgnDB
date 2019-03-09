package com.engwork.pgndb.chessgamesloader.mappers;

import com.engwork.pgndb.chessgamesconverter.model.Event;
import java.util.List;
import java.util.UUID;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface EventsMapper {
  String insertEvent = "insert into events(id,name,place) values(#{id},#{name},#{place})";
  String updateEvent = "update events set name=#{name},place=#{place} where id=#{id}";
  String deleteEventById = "delete from events where id=#{id}";
  String selectEventId = "select id from events where name=#{name} and place=#{place}";

  @Select("SELECT * FROM events WHERE id= #{id}")
  @Results(
      value = {
          @Result(property = "id", column = "id"),
          @Result(property = "name", column = "name"),
          @Result(property = "place", column = "place")
      }
  )
  Event selectEventById(UUID id);

  @Insert(insertEvent)
  void insertEvent(Event event);

  @Update(updateEvent)
  void updateEvent(Event event);

  @Delete(deleteEventById)
  void deleteEventById(UUID id);

  @Select(selectEventId)
  UUID selectEventId(Event event);

  @Insert({
      "<script>",
      "INSERT INTO events",
      "(id, name, place)",
      "VALUES <foreach item='item_name' collection='events' open='' separator=',' close=''>",
      "(#{item_name.id},",
      "#{item_name.name},",
      "#{item_name.place})",
      "</foreach>",
      " ON CONFLICT (id) DO NOTHING",
      "</script>"
  })
  void insertMany(@Param("events") List<Event> events);

  //  SEARCHING
  @Select("SELECT id FROM events WHERE LOWER(name) LIKE '%' || LOWER(#{name}) || '%'")
  List<UUID> selectEventsIdsByName(@Param("name") String name);

  @Select("SELECT id FROM events WHERE LOWER(place) LIKE '%' || LOWER(#{site}) || '%'")
  List<UUID> selectEventsIdsBySite(@Param("site") String site);

  @Select("SELECT id FROM events WHERE LOWER(name) LIKE '%' || LOWER(#{name}) || '%' AND LOWER(place) LIKE '%' || LOWER(#{site}) || '%'")
  List<UUID> selectEventIdsByNameAndSite(@Param("name") String name, @Param("site") String site);

}
