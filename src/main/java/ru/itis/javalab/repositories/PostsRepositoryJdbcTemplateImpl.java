package ru.itis.javalab.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.itis.javalab.dto.PostShowingDto;
import ru.itis.javalab.models.Post;

import javax.sql.DataSource;
import java.util.List;

public class PostsRepositoryJdbcTemplateImpl implements PostsRepository {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final String SQL_INSERT_POST= "insert into posts(user_id, post_title, post_text) values (?, ?, ?)";
    private static final String SQL_SELECT_ALL = "select * from posts";
    private static final String SQL_SELECT_ALL_WITH_USERS = "select posts.id ,first_name, last_name, post_text, post_title, likes, dislikes, email, image_path from posts join users u on posts.user_id = u.id";


    private final RowMapper<Post> postRowMapper = (row, i) -> Post.builder()
            .id(row.getLong("id"))
            .userId(row.getLong("user_id"))
            .title(row.getString("post_title"))
            .text(row.getString("post_text"))
            .likes(row.getInt("likes"))
            .dislikes(row.getInt("dislikes"))
            .build();

    private final RowMapper<PostShowingDto> postShowingMapper = (row, i) -> PostShowingDto.builder()
            .id(row.getInt("id"))
            .firstName(row.getString("first_name"))
            .lastName(row.getString("last_name"))
            .image_path(row.getString("image_path"))
            .email(row.getString("email"))
            .title(row.getString("post_title"))
            .text(row.getString("post_text"))
            .likes(row.getInt("likes"))
            .dislikes(row.getInt("dislikes"))
            .build();



    public PostsRepositoryJdbcTemplateImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }



    public List<PostShowingDto> findAllWithUsers() {
        return jdbcTemplate.query(SQL_SELECT_ALL_WITH_USERS, postShowingMapper);
    }

    @Override
    public List<Post> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL, postRowMapper);
    }

    @Override
    public void save(Post entity) {
        jdbcTemplate.update(SQL_INSERT_POST, entity.getUserId(), entity.getTitle(), entity.getText());
    }

    @Override
    public void update(Post entity) {

    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void delete(Post entity) {

    }


}