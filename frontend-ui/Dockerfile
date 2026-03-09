FROM node:18 AS build
WORKDIR /app
COPY frontend-ui/package*.json ./frontend-ui/
WORKDIR /app/frontend-ui
RUN npm install
COPY frontend-ui/ .
RUN npm run build

FROM nginx:stable-alpine
COPY --from=build /app/frontend-ui/build /usr/share/nginx/html
COPY frontend-ui/nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
