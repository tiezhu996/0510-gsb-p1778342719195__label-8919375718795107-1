-- FruitShop Initial Data Script
-- 设置字符集
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET character_set_connection=utf8mb4;

USE fruitshop;

-- 初始化分类数据
INSERT INTO `category` (`id`, `name`, `icon`, `sort`) VALUES
(1, '热带水果', 'https://images.unsplash.com/photo-1587132137056-bfbf0166836e?w=100&auto=format&fit=crop&q=60', 1),
(2, '浆果类', 'https://images.unsplash.com/photo-1498557850523-fd3d118b962e?w=100&auto=format&fit=crop&q=60', 2),
(3, '柑橘类', 'https://images.unsplash.com/photo-1547514701-42782101795e?w=100&auto=format&fit=crop&q=60', 3),
(4, '核果类', 'https://images.unsplash.com/photo-1629226182720-f0a169fc9a8e?w=100&auto=format&fit=crop&q=60', 4),
(5, '瓜果类', 'https://images.unsplash.com/photo-1587049352846-4a222e784d38?w=100&auto=format&fit=crop&q=60', 5),
(6, '进口水果', 'https://images.unsplash.com/photo-1528821154947-1aa3d1b74941?w=100&auto=format&fit=crop&q=60', 6);

-- 初始化水果数据
INSERT INTO `fruit` (`id`, `category_id`, `name`, `description`, `price`, `original_price`, `unit`, `stock`, `sales`, `main_image`, `images`, `origin`, `is_hot`, `is_new`) VALUES
(1, 6, '智利进口车厘子', 'JJ级大果，果肉饱满，甜度高', 99.90, 129.90, '斤', 500, 2356,
 'https://images.unsplash.com/photo-1528821154947-1aa3d1b74941?w=400&auto=format&fit=crop&q=80',
 '["https://images.unsplash.com/photo-1528821154947-1aa3d1b74941?w=800&auto=format&fit=crop&q=80","https://images.unsplash.com/photo-1528820601132-0e8ace6f72b7?w=800&auto=format&fit=crop&q=80"]',
 '智利', 1, 0),
(2, 1, '泰国金枕头榴莲', '香甜软糯，果肉厚实，榴莲控必选', 199.00, 259.00, '个', 200, 1823,
 'https://images.unsplash.com/photo-1562486683-67d4d5886f99?w=400&auto=format&fit=crop&q=80',
 '["https://images.unsplash.com/photo-1562486683-67d4d5886f99?w=800&auto=format&fit=crop&q=80","https://images.unsplash.com/photo-1588482415422-a061902e00a4?w=800&auto=format&fit=crop&q=80"]',
 '泰国', 1, 0),
(3, 5, '新疆哈密瓜', '脆甜爽口，香气浓郁，西域阳光的味道', 29.90, 39.90, '个', 800, 3256,
 'https://images.unsplash.com/photo-1581375074612-d1fd0e661aeb?w=400&auto=format&fit=crop&q=80',
 '["https://images.unsplash.com/photo-1581375074612-d1fd0e661aeb?w=800&auto=format&fit=crop&q=80"]',
 '新疆', 1, 0),
(4, 2, '云南蓝莓', '花青素丰富，酸甜可口，护眼佳品', 39.90, 49.90, '盒', 600, 1532,
 'https://images.unsplash.com/photo-1498557850523-fd3d118b962e?w=400&auto=format&fit=crop&q=80',
 '["https://images.unsplash.com/photo-1498557850523-fd3d118b962e?w=800&auto=format&fit=crop&q=80","https://images.unsplash.com/photo-1606757389667-45c2024f9fa4?w=800&auto=format&fit=crop&q=80"]',
 '云南', 1, 1),
(5, 1, '海南金煌芒果', '果肉细腻，甜度高，芒果中的贵族', 35.90, 45.90, '斤', 400, 2156,
 'https://images.unsplash.com/photo-1553279768-865429fa0078?w=400&auto=format&fit=crop&q=80',
 '["https://images.unsplash.com/photo-1553279768-865429fa0078?w=800&auto=format&fit=crop&q=80"]',
 '海南', 0, 1),
(6, 1, '越南红心火龙果', '清甜爽口，营养丰富，高颜值水果', 12.90, 16.90, '个', 1000, 5621,
 'https://images.unsplash.com/photo-1527325678964-54921661f888?w=400&auto=format&fit=crop&q=80',
 '["https://images.unsplash.com/photo-1527325678964-54921661f888?w=800&auto=format&fit=crop&q=80"]',
 '越南', 0, 0),
(7, 3, '赣南脐橙', '皮薄多汁，酸甜适中，橙中极品', 19.90, 25.90, '斤', 800, 4532,
 'https://images.unsplash.com/photo-1547514701-42782101795e?w=400&auto=format&fit=crop&q=80',
 '["https://images.unsplash.com/photo-1547514701-42782101795e?w=800&auto=format&fit=crop&q=80"]',
 '江西', 0, 0),
(8, 2, '丹东草莓', '鲜嫩多汁，香甜可口，草莓界的天花板', 49.90, 59.90, '盒', 300, 1876,
 'https://images.unsplash.com/photo-1464965911861-746a04b4bca6?w=400&auto=format&fit=crop&q=80',
 '["https://images.unsplash.com/photo-1464965911861-746a04b4bca6?w=800&auto=format&fit=crop&q=80"]',
 '辽宁', 0, 1),
(9, 4, '水蜜桃', '果肉软嫩，汁水丰富，入口即化', 25.90, 32.90, '斤', 500, 2234,
 'https://images.unsplash.com/photo-1629226182720-f0a169fc9a8e?w=400&auto=format&fit=crop&q=80',
 '["https://images.unsplash.com/photo-1629226182720-f0a169fc9a8e?w=800&auto=format&fit=crop&q=80"]',
 '浙江', 0, 0),
(10, 5, '西瓜', '皮薄肉甜，消暑解渴，夏日必备', 15.90, 19.90, '个', 600, 6234,
 'https://images.unsplash.com/photo-1587049352846-4a222e784d38?w=400&auto=format&fit=crop&q=80',
 '["https://images.unsplash.com/photo-1587049352846-4a222e784d38?w=800&auto=format&fit=crop&q=80"]',
 '山东', 0, 0);

-- 初始化规格数据
INSERT INTO `fruit_spec` (`id`, `fruit_id`, `name`, `price`, `stock`) VALUES
(1, 1, '1斤装', 59.90, 200),
(2, 1, '2斤装', 99.90, 200),
(3, 1, '5斤装', 199.90, 100),
(4, 2, '3-4斤/个', 159.00, 100),
(5, 2, '4-5斤/个', 199.00, 100),
(6, 4, '125g×2盒', 39.90, 300),
(7, 4, '125g×4盒', 69.90, 300);

-- 初始化测试用户 (密码: password123)
INSERT INTO `user` (`id`, `phone`, `password`, `nickname`, `avatar`) VALUES
(1, '13800138000', '$2a$10$w0nGGcfPb4V7/7SE240Doejs3B4pLK3FpnzL/83i/gFE91GW7.ZzG', '测试用户',
 'https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=100&auto=format&fit=crop&q=80');

-- 初始化测试地址
INSERT INTO `address` (`id`, `user_id`, `name`, `phone`, `province`, `city`, `district`, `detail`, `is_default`) VALUES
(1, 1, '张三', '13800138000', '北京市', '北京市', '朝阳区', '建国路88号SOHO现代城A座2001室', 1),
(2, 1, '李四', '13900139000', '上海市', '上海市', '浦东新区', '世纪大道1号陆家嘴金融中心1502室', 0);
