CREATE DATABASE `banquemisr` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;

    -- banquemisr.users definition

CREATE TABLE `users` (
                         `id` int(11) NOT NULL AUTO_INCREMENT,
                         `name` varchar(255) DEFAULT NULL,
                         `email` varchar(255) DEFAULT NULL,
                         `password` varchar(255) DEFAULT NULL,
                         `role` varchar(255) DEFAULT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


-- banquemisr.notifications definition

CREATE TABLE `notifications` (
                                 `id` int(11) NOT NULL AUTO_INCREMENT,
                                 `subject` varchar(600) NOT NULL,
                                 `text` varchar(2000) NOT NULL,
                                 `email` varchar(255) NOT NULL,
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


-- banquemisr.tasks definition

CREATE TABLE `tasks` (
                         `id` bigint(20) NOT NULL AUTO_INCREMENT,
                         `title` varchar(255) NOT NULL,
                         `description` varchar(255) DEFAULT NULL,
                         `status` varchar(255) DEFAULT NULL,
                         `priority` varchar(255) DEFAULT NULL,
                         `due_date` datetime(6) DEFAULT NULL,
                         `user_id` int(11) DEFAULT NULL,
                         PRIMARY KEY (`id`),
                         KEY `tasks_FK` (`user_id`),
                         CONSTRAINT `tasks_FK` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


INSERT INTO banquemisr.users (name,email,password,`role`) VALUES
                                                              ('admin','admin@banquemasr.com','$2b$12$Pfi11VqBKwyOYFqnYeihrOJINCZr/9bFZCIOKIWcNvxUyfKOtvj/.','ROLE_ADMIN'),
                                                              ('user','user@banquemasr.com','$2b$12$Pfi11VqBKwyOYFqnYeihrOJINCZr/9bFZCIOKIWcNvxUyfKOtvj/.','ROLE_USER');
INSERT INTO banquemisr.tasks (title,description,status,priority,due_date,user_id) VALUES
    ('Task1','Task1 Decription','Pending','High','2024-12-01 15:30:00.0',1);