CREATE TABLE profile (
                         id UUID PRIMARY KEY,
                         user_id UUID NOT NULL UNIQUE,
                         username VARCHAR(255) NOT NULL UNIQUE,
                         full_name VARCHAR(255) NOT NULL,
                         phone_number VARCHAR(50),
                         bio TEXT,
                         profile_picture_url VARCHAR(500),
                         cover_picture_url VARCHAR(500),
                         location VARCHAR(255),
                         created_at TIMESTAMP NOT NULL,
                         updated_at TIMESTAMP NOT NULL,
                         deleted_at TIMESTAMP
);

CREATE INDEX idx_profile_user_id ON profile(user_id);
CREATE INDEX idx_profile_username ON profile(username);