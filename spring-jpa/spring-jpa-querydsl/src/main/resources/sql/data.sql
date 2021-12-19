INSERT INTO tb_company (company_id, name) VALUES ('company01', 'sample company1');
INSERT INTO tb_company (company_id, name) VALUES ('company02', 'sample company2');

INSERT INTO tb_user (user_id, name, password, company_id) VALUES ('user01', 'name01', 'password01', 'company01');
INSERT INTO tb_user (user_id, name, password, company_id) VALUES ('user02', 'name02', 'password02', 'company01');
INSERT INTO tb_user (user_id, name, password, company_id) VALUES ('user03', 'name03', 'password03', 'company01');
INSERT INTO tb_user (user_id, name, password, company_id) VALUES ('user04', 'name04', 'password04', 'company01');
INSERT INTO tb_user (user_id, name, password, company_id) VALUES ('user05', 'name05', 'password05', 'company01');

INSERT INTO tb_user (user_id, name, password, company_id) VALUES ('user06', 'name06', 'password06', 'company02');
INSERT INTO tb_user (user_id, name, password, company_id) VALUES ('user07', 'name07', 'password07', 'company02');

INSERT INTO tb_statistics (statistics_id, year, month, day, data, view_count) VALUES ('statistics01', '2021', '12', '01', 'data1', 1);
INSERT INTO tb_statistics (statistics_id, year, month, day, data, view_count) VALUES ('statistics02', '2021', '12', '02', 'data2', 2);
INSERT INTO tb_statistics (statistics_id, year, month, day, data, view_count) VALUES ('statistics03', '2021', '12', '03', 'data3', 3);
INSERT INTO tb_statistics (statistics_id, year, month, day, data, view_count) VALUES ('statistics04', '2021', '12', '04', 'data4', 4);
INSERT INTO tb_statistics (statistics_id, year, month, day, data, view_count) VALUES ('statistics05', '2021', '12', '05', 'data5', 5);
INSERT INTO tb_statistics (statistics_id, year, month, day, data, view_count) VALUES ('statistics06', '2021', '12', '06', 'data6', 6);
INSERT INTO tb_statistics (statistics_id, year, month, day, data, view_count) VALUES ('statistics07', '2021', '12', '07', 'data7', 7);