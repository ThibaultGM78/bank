# Build
FROM node:24-alpine AS build
WORKDIR /app
COPY . .
RUN npm install
RUN npm run build -- --configuration production

# Serveur Nginx
FROM nginx:alpine
COPY --from=build /app/dist/bank/browser /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]