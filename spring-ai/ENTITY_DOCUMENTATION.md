# Entity 类文档

本文档描述了项目中所有的实体类及其对应的数据库表结构。

## 1. User (用户实体类)
**对应表：** `users`
**文件路径：** `src/main/java/cn/sdtbu/edu/entity/User.java`

### 字段说明：
- `id` (Integer) - 用户ID，主键，自增
- `username` (String) - 用户名，不能为空，唯一
- `email` (String) - 邮箱，不能为空，唯一
- `password` (String) - 密码，不能为空
- `avatar` (String) - 头像URL
- `bio` (String) - 个人简介
- `createdAt` (Timestamp) - 创建时间，插入时自动填充

## 2. Category (电台节目分类实体类)
**对应表：** `categories`
**文件路径：** `src/main/java/cn/sdtbu/edu/entity/Category.java`

### 字段说明：
- `id` (Integer) - 分类ID，主键，自增
- `name` (String) - 分类名称，唯一，不能为空，最大长度100
- `description` (String) - 分类描述
- `createdAt` (Timestamp) - 创建时间，插入时自动填充

## 3. RadioProgram (电台节目信息实体类)
**对应表：** `radio_programs`
**文件路径：** `src/main/java/cn/sdtbu/edu/entity/RadioProgram.java`

### 字段说明：
- `id` (Integer) - 节目ID，主键，自增
- `title` (String) - 节目名称，不能为空
- `description` (String) - 节目简介
- `audioUrl` (String) - 音频文件访问URL，不能为空
- `coverImageUrl` (String) - 封面图片URL
- `categoryId` (Integer) - 分类ID，外键关联categories表
- `artistNarrator` (String) - 艺术家/播讲人/主播名
- `album` (String) - 所属专辑/系列（可选）
- `durationSeconds` (Integer) - 节目时长（秒）
- `tags` (String) - 标签，逗号分隔（如：冥想,放松,正念）
- `publicationDate` (Date) - 发布日期（如果节目有原始发布日期）
- `playsCount` (Integer) - 播放次数（应用内统计），默认为0
- `likesCount` (Integer) - 喜欢次数（应用内统计），默认为0
- `commentsCount` (Integer) - 评论数量（应用内统计），默认为0
- `isFeatured` (Boolean) - 是否精选/推荐：1是，0否，默认为0
- `status` (String) - 节目状态：published-已发布，draft-草稿，archived-已归档，默认为published
- `createdAt` (Timestamp) - 数据入库时间，插入时自动填充
- `updatedAt` (Timestamp) - 更新时间，插入和更新时自动填充

## 4. Comment (用户对节目的评论实体类)
**对应表：** `comments`
**文件路径：** `src/main/java/cn/sdtbu/edu/entity/Comment.java`

### 字段说明：
- `id` (Integer) - 评论ID，主键，自增
- `userId` (Integer) - 用户ID，外键关联users表
- `programId` (Integer) - 节目ID，外键关联radio_programs表
- `parentCommentId` (Integer) - 父评论ID，用于回复，可为空
- `content` (String) - 评论内容，不能为空
- `createdAt` (Timestamp) - 创建时间，插入时自动填充
- `updatedAt` (Timestamp) - 更新时间，插入和更新时自动填充

## 5. Playlist (用户创建的歌单实体类)
**对应表：** `playlists`
**文件路径：** `src/main/java/cn/sdtbu/edu/entity/Playlist.java`

### 字段说明：
- `id` (Integer) - 歌单ID，主键，自增
- `userId` (Integer) - 用户ID，外键关联users表
- `name` (String) - 歌单名称，不能为空，最大长度100
- `description` (String) - 歌单描述
- `isPublic` (Boolean) - 是否公开：1是，0否，默认为1
- `createdAt` (Timestamp) - 创建时间，插入时自动填充
- `updatedAt` (Timestamp) - 更新时间，插入和更新时自动填充

## 6. PlaylistItem (歌单包含的节目实体类)
**对应表：** `playlist_items`
**文件路径：** `src/main/java/cn/sdtbu/edu/entity/PlaylistItem.java`

### 字段说明：
- `id` (Integer) - 歌单项ID，主键，自增
- `playlistId` (Integer) - 歌单ID，外键关联playlists表
- `programId` (Integer) - 节目ID，外键关联radio_programs表
- `displayOrder` (Integer) - 在歌单中的显示顺序，默认为0
- `addedAt` (Timestamp) - 添加时间，插入时自动填充

### 约束：
- 唯一约束：`uq_playlist_program` (playlist_id, program_id)

## 7. UserProgramLike (用户喜欢的电台节目实体类)
**对应表：** `user_program_likes`
**文件路径：** `src/main/java/cn/sdtbu/edu/entity/UserProgramLike.java`

### 字段说明：
- `userId` (Integer) - 用户ID，联合主键的一部分
- `programId` (Integer) - 节目ID，联合主键的一部分
- `createdAt` (Timestamp) - 创建时间，插入时自动填充

### 特殊说明：
- 该表使用联合主键 (user_id, program_id)
- 由于MyBatis-Plus不支持复合主键，需要在Mapper中自定义SQL来处理联合主键的操作
- 重写了equals、hashCode和toString方法

## 8. VerificationCode (验证码实体类)
**对应表：** `verification_codes`
**文件路径：** `src/main/java/cn/sdtbu/edu/entity/VerificationCode.java`

### 字段说明：
- `id` (Integer) - 验证码ID，主键，自增
- `email` (String) - 用户邮箱
- `code` (String) - 验证码
- `createdAt` (Timestamp) - 创建时间，插入时自动填充
- `expiresAt` (Timestamp) - 过期时间

## 技术说明

### 使用的注解：
- `@TableName` - 指定对应的数据库表名
- `@TableId` - 指定主键字段
- `@TableField` - 指定字段映射和填充策略
- `@Getter/@Setter` - Lombok注解，自动生成getter和setter方法

### 字段填充策略：
- `FieldFill.INSERT` - 仅在插入时填充
- `FieldFill.INSERT_UPDATE` - 在插入和更新时都填充

### 数据类型说明：
- 使用 `java.sql.Timestamp` 而不是 `LocalDateTime` 以保持与数据库的兼容性
- 使用 `java.sql.Date` 用于日期字段
- 主键类型统一使用 `Integer`

## 注意事项

1. **联合主键处理**：`UserProgramLike` 表由于使用联合主键，需要在对应的Mapper中编写自定义SQL。

2. **字段命名**：数据库字段使用下划线命名（如 `user_id`），Java字段使用驼峰命名（如 `userId`）。

3. **外键关系**：实体类中使用Integer类型存储外键ID，而不是直接引用其他实体对象，这样可以避免循环依赖和性能问题。

4. **枚举字段**：`RadioProgram.status` 字段在数据库中定义为枚举类型，但在Java中使用String类型处理。

5. **自动填充**：创建时间和更新时间字段配置了自动填充，需要配置相应的MetaObjectHandler。
