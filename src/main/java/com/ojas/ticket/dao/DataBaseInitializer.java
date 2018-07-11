/*package com.ojas.ticket.dao;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.ojas.ticket.dao.blogpost.BlogPostDao;
import com.ojas.ticket.dao.user.UserDao;


public class DataBaseInitializer
{
    private BlogPostDao blogPostDao;

    private UserDao userDao;

    private PasswordEncoder passwordEncoder;

    protected DataBaseInitializer()
    {
        
    }

    public DataBaseInitializer(UserDao userDao, BlogPostDao blogPostDao, PasswordEncoder passwordEncoder)
    {
        this.userDao = userDao;
        this.blogPostDao = blogPostDao;
        this.passwordEncoder = passwordEncoder;
    }

    public void initDataBase()
    {
        User userUser = new User("user", this.passwordEncoder.encode("user"));
        userUser.addRole(Role.USER);
        this.userDao.save(userUser);

        User adminUser = new User("admin", this.passwordEncoder.encode("admin"));
        adminUser.addRole(Role.USER);
        adminUser.addRole(Role.ADMIN);
        this.userDao.save(adminUser);

        long timestamp = System.currentTimeMillis() - (1000 * 60 * 60 * 24);
        for (int i = 0; i < 10; i++) {
            BlogPost blogPost = new BlogPost();
            blogPost.setContent("This is example content " + i);
            blogPost.setDate(new Date(timestamp));
            this.blogPostDao.save(blogPost);
            timestamp += 1000 * 60 * 60;
        }
    }
}
*/