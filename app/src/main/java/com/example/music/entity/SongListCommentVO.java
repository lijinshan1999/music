package com.example.music.entity;

public class SongListCommentVO {

    private String commentId;
    private String content;
    private String timeStr;
    private User user;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "SongListCommentVO{" +
                "commentId='" + commentId + '\'' +
                ", content='" + content + '\'' +
                ", timeStr='" + timeStr + '\'' +
                ", user=" + user +
                '}';
    }

    public class User{
        private String userId;
        private String nickname;
        private String avatarUrl;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatarUrl() {
            return avatarUrl;
        }

        public void setAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
        }

        @Override
        public String toString() {
            return "User{" +
                    "userId='" + userId + '\'' +
                    ", nickname='" + nickname + '\'' +
                    ", avatarUrl='" + avatarUrl + '\'' +
                    '}';
        }
    }


}
