# DataHub 部署说明

## 后端部署 Render / Railway

1. 在 Render 或 Railway 新建 MySQL 8.0 数据库，记录 `MYSQL_HOST`、`MYSQL_PORT`、`MYSQL_DATABASE`、`MYSQL_USER`、`MYSQL_PASSWORD`。
2. 新建后端 Web Service，根目录选择 `backend/`，运行 Java 17。
3. 配置后端环境变量：
   - `SPRING_DATASOURCE_URL=jdbc:mysql://<MYSQL_HOST>:<MYSQL_PORT>/<MYSQL_DATABASE>?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true`
   - `SPRING_DATASOURCE_USERNAME=<MYSQL_USER>`
   - `SPRING_DATASOURCE_PASSWORD=<MYSQL_PASSWORD>`
   - `JWT_SECRET=<至少32位随机字符串>`
4. 部署完成后，记录后端公网地址，例如 `https://datahub-api.onrender.com`。

## 前端部署 GitHub Pages

1. 修改 `frontend/.env.production`：
   - `VITE_API_BASE_URL=https://你的后端公网域名/api`
2. 推送代码到 `main` 或 `master` 分支。
3. GitHub Actions 会自动构建 `frontend/dist` 并发布到 `gh-pages` 分支。
4. 在仓库 `Settings -> Pages` 中选择 `Deploy from a branch`，分支选择 `gh-pages`，目录选择 `/root`。

## 前端部署 Vercel

1. 在 Vercel 导入 GitHub 仓库，Framework Preset 选择 `Vite`。
2. Root Directory 填写 `frontend`。
3. Build Command 填写 `npm run build`，Output Directory 填写 `dist`。
4. 在 Vercel 环境变量中添加：
   - `VITE_API_BASE_URL=https://你的后端公网域名/api`
5. 重新部署前端。
