模块一：电台发现 (Radio Discovery)
1. 主页节目展示
* 目的： 展示电台节目列表，可分页，可按不同条件排序（最新、最热、精选等）。
* 后端 API：
* GET /api/programs
* 请求参数： page, limit, sortBy (如 createdAt_desc, playsCount_desc, likesCount_desc, isFeatured_desc_createdAt_desc), categoryId, tag (新增，按标签筛选)
* 逻辑：
1. 从 radio_programs 查询状态为 published 的数据。
2. 可 JOIN categories 获取分类名称。
3. 根据 sortBy 参数排序 (例如，如果 sortBy=isFeatured_desc_createdAt_desc，则 ORDER BY is_featured DESC, created_at DESC)。
4. 如果提供了 categoryId，则 WHERE category_id = ?。
5. 如果提供了 tag，则 WHERE FIND_IN_SET(?, tags) (注意 FIND_IN_SET 的效率，大量数据时可能考虑更优方案如标签表)。
6. 实现分页。
* 响应： 节目列表数组（包含 title, cover_image_url, artist_narrator, likes_count, plays_count 等），总数，当前页等。


2. 节目详情与播放
* 目的： 显示单个节目的详细信息并提供播放功能。
* 后端 API：
* GET /api/programs/{programId}
* 逻辑：
1. 根据 programId 从 radio_programs 查询节目详情 (确保 status = 'published' 或允许管理员预览 draft)。
2. JOIN categories 获取分类名称。
* 响应： 节目对象（包含所有详细信息如 title, description, audio_url, cover_image_url, artist_narrator, duration_seconds, tags, category_name 等）。
sql SELECT rp.*, c.name as category_name FROM radio_programs rp LEFT JOIN categories c ON rp.category_id = c.id WHERE rp.id = ? AND rp.status = 'published'; -- 或根据用户角色调整status条件
* POST /api/programs/{programId}/play (用于统计播放，逻辑不变)

模块二：个性化互动 (Personalized Interaction)
3. 节目喜欢/取消喜欢
* 目的： 用户可以喜欢或取消喜欢一个节目。
* 后端 API (逻辑基本不变，但注意操作的是 radio_programs.likes_count)：
* POST /api/programs/{programId}/like
* DELETE /api/programs/{programId}/like
* 逻辑：
* 用户认证。
* 操作 user_program_likes 表。
* 对应更新 radio_programs 表的 likes_count 字段。

4. 创建与管理歌单
* 目的： 用户创建、查看、编辑、删除自己的歌单。
* 后端 API (逻辑不变，操作 playlists 表)：
* POST /api/playlists
* GET /api/playlists (获取当前用户的歌单)
* GET /api/playlists/{playlistId} (获取歌单详情，同时应包含歌单内的节目信息，见下一点)
* PUT /api/playlists/{playlistId}
* DELETE /api/playlists/{playlistId}


5. 歌单内容管理 (增删改查节目到歌单)
* 目的： 向歌单中添加、移除节目，调整节目顺序。
* 后端 API (逻辑不变，操作 playlist_items 表，关联 radio_programs 获取节目信息)：
* POST /api/playlists/{playlistId}/items (添加节目)
* 请求体：{ "program_id": 123 }
* GET /api/playlists/{playlistId}/items (通常在 GET /api/playlists/{playlistId} 中一并返回)
* DELETE /api/playlists/{playlistId}/items/{itemId} (移除节目)
* PUT /api/playlists/{playlistId}/items/order (调整顺序)

模块三：社区交流 (Community Exchange)
6. 节目评论与查看
* 目的： 用户对节目发表评论，查看他人评论。
* 后端 API (逻辑基本不变，但注意操作的是 radio_programs.comments_count)：
* POST /api/programs/{programId}/comments
* 逻辑：插入 comments 表，并 UPDATE radio_programs SET comments_count = comments_count + 1 WHERE id = ?;
* GET /api/programs/{programId}/comments
* 逻辑：查询 comments 表，JOIN users 获取评论者信息。


7. 评论回复
* 目的： 用户可以回复某条评论。
* 后端 API (逻辑不变，操作 comments 表，parent_comment_id 字段)：
* POST /api/programs/{programId}/comments (parent_comment_id 有值)
* GET /api/comments/{commentId}/replies


模块四：我的空间 (My Space)
8. 个人中心
* 目的： 展示用户个人资料、喜欢的节目、创建的歌单、发表的评论。
* 后端 API (逻辑基本不变，查询时注意关联的 radio_programs 表字段)：
* GET /api/me (获取用户信息)
* PUT /api/me (编辑用户信息)
* GET /api/me/liked-programs
* GET /api/me/playlists (即 GET /api/playlists 过滤当前用户)
* GET /api/me/comments



9. 节目搜索
* 目的： 用户通过关键词搜索电台节目。
* 后端 API：
* GET /api/programs/search
* 请求参数： q (关键词), page, limit
* 逻辑：
1. q 可以用于匹配 radio_programs.title, radio_programs.description, radio_programs.artist_narrator, radio_programs.tags, 甚至 categories.name。
2. 确保只搜索 status = 'published' 的节目。
* 响应： 匹配的节目列表，分页信息。


主要影响和注意事项：
数据填充： 由于电台节目是预先存在的，你需要有机制将这些节目的元数据（包括 audio_url, cover_image_url, artist_narrator, duration_seconds, tags 等）录入到 radio_programs 表中。这可能是通过数据库管理工具手动插入，或者编写脚本批量导入。
URL有效性： 应用层面不负责音频和图片的存储，所以要确保 audio_url 和 cover_image_url 是长期有效的公共可访问链接。
内容管理： radio_programs 表中的 status (published, draft, archived) 和 is_featured 字段对于后台内容管理非常有用。你可能需要一个简单的管理界面来维护这些节目的状态和推荐。
元数据利用： 新增的 artist_narrator, tags 等字段应该在前端展示，并在搜索、筛选时加以利用，以提升用户体验。
性能：
对于 tags 字段的 FIND_IN_SET(?, tags) 或 tags LIKE ? 查询，如果标签数量很多或者节目数量巨大，性能可能会下降。如果标签功能变得非常重要且复杂，未来可以考虑将 tags 拆分成一个单独的 tags 表和一个 program_tags 关联表 (多对多关系)，这样查询效率更高，管理也更规范。但初期逗号分隔字符串是快速实现的方式。
确保 radio_programs 表上常用的查询字段（如 status, is_featured, category_id, artist_narrator）建立索引。
