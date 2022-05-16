-- password 평문
INSERT INTO tb_user (user_id, name, data) VALUES ('user01', 'name01', 'non-encrypt');

-- password 암호화 (1234)
INSERT INTO tb_user (user_id, name, data) VALUES ('user02', 'name02', 'usZLPCiDsM0QfN0pPhH/jg==');
