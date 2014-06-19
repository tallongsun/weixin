SET CHARACTER_SET_CLIENT=utf8;
SET CHARACTER_SET_CONNECTION=utf8;

DROP DATABASE IF EXISTS `chat`;
CREATE DATABASE `chat` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `chat`;

drop table if EXISTS `knowledge`;
drop table if EXISTS `knowledge_sub`;
drop table if EXISTS `joke`;
drop table if EXISTS `chat_log`;

create table `knowledge`(
	`id` int not null primary key comment '主键标识',
	`question` varchar(2000) not null comment '问题',
	`answer` text(8000) not null comment '答案',
	`category` int not null comment '知识的类别（1:普通对话 2:笑话 3:上下文）'
) comment='问答知识表';
insert into knowledge values(1 , '我不开心,好难受,心里烦', '', 1);
insert into knowledge values(2 , '哈哈,嘻嘻,嘿嘿,呵呵', '', 1);
insert into knowledge values(3 , '你知道的真多，好聪明', '', 1);
insert into knowledge values(4, '给我讲个笑话', '', 2);
insert into knowledge values(5 , '继续', '我们聊到哪了？', 3);
insert into knowledge values(6 , '再来一个', '那你再给点掌声吧', 3);
insert into knowledge values(7, '还有吗', '你是指什么呢？', 3);
insert into knowledge values(8 , 'Hi,Hello,嗨,你好', '你好，很高兴认识你。', 1);
insert into knowledge values(9 , '你的主人/老板/发明者是谁', '我老板是tallong', 1);
insert into knowledge values(10 , '你觉得我帅吗', '还行吧，比我还差那么一点点。', 1);
insert into knowledge values(11 , '你在干什么呢', '我在专心和你聊天啊', 1);
insert into knowledge values(12 , '你喜欢我吗', '中国的首都是北京。', 1);
insert into knowledge values(13 , '中国的首都是哪', '中国的首都是北京。', 1);
insert into knowledge values(14 , '明天又要上班了', '好好工作哦~', 1);
insert into knowledge values(15 , '我好饿', '那快吃饭去啊', 1);
insert into knowledge values(16 , '什么是幸福', '幸福是一种感觉。', 1);
insert into knowledge values(17 , '你是机器人吗', '是啊，很智能的那种哦~', 1);
insert into knowledge values(18 , '唉,哎', '怎么了，叹什么气呢？', 1);

create table `knowledge_sub`(
	`id` int not null auto_increment primary key comment '主键标识',
	`pid` int not null comment '与knowledge表中的id相对应',
	`answer` text(8000) not null comment '答案'
) comment='问答知识子表';
insert into knowledge_sub(pid, answer) values(1, '看到我你就开心了');
insert into knowledge_sub(pid, answer) values(1, '有什么不开心的说来听听');
insert into knowledge_sub(pid, answer) values(1, '那我陪你聊聊天吧');
insert into knowledge_sub(pid, answer) values(1, '别难受了，我会一直陪着你的。');
insert into knowledge_sub(pid, answer) values(2, '看来你今天心情不错啊');
insert into knowledge_sub(pid, answer) values(2, '嘿嘿');
insert into knowledge_sub(pid, answer) values(2, '哈哈');
insert into knowledge_sub(pid, answer) values(2, '嘻嘻');
insert into knowledge_sub(pid, answer) values(2, '什么事这么好笑？');
insert into knowledge_sub(pid, answer) values(3, '我认为你说的很有道理');
insert into knowledge_sub(pid, answer) values(3, '因为我是聪明的机器人呀');
insert into knowledge_sub(pid, answer) values(3, '这是天生的，没办法，哈哈。');
insert into knowledge_sub(pid, answer) values(3, '我会努力变得更加聪明的');

create table `joke`(
	`joke_id` int(8) primary key not null auto_increment comment '笑话id',
	`joke_content` text(8000) comment '笑话内容'
) comment='笑话表';
insert into joke(joke_content) values('一日一醉汉酒后打车回家，伸手拦一辆110巡警车，并且嚷嚷道：就算你是每公里一块一，也没必要写那么大嘛！');
insert into joke(joke_content) values('公共汽车上老太太怕坐过站，逢站必问。汽车到一站，她一个劲的用雨伞捅司机“这是展览中心吗？”“不是，这是排骨！”');
insert into joke(joke_content) values('课堂上老师点名：“刘华!” 结果下面一孩子大声回到：“yeah!” 老师很生气：“为什么不说‘到’？” 孩子说：“那个字念‘烨’……”。');
insert into joke(joke_content) values('昨天被公司美女同事莫名的亲了一口，心里各种的爽。后来才知道人家玩真心话大冒险，是叫亲一个公司最丑的，最丑的！');
insert into joke(joke_content) values('有个人第一次在集市上卖冰棍，不好意思叫卖，旁边有一个人正高声喊：“卖冰棍”，他只好喊道：“我也是”。');

create table `chat_log`(
	`id` int not null auto_increment primary key comment '主键标识',
	`open_id` varchar(30) not null comment '用户的OpenID',
	`create_time` varchar(20) not null comment '消息创建时间',
	`req_msg` varchar(2000) not null comment '用户上行的消息',
	`resp_msg` varchar(2000) not null comment '公众账号回复的消息',
	`chat_category` int comment '聊天的类别（0:未知 1:普通对话 2:笑话 3:上下文）'
) comment='聊天记录表';