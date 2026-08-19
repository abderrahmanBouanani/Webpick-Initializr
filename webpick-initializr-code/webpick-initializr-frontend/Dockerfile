# Stage 1: Build Angular application
FROM node:20-alpine AS builder

WORKDIR /app

# Copy package manifests
COPY package.json package-lock.json ./

# Install exact dependencies
RUN npm ci

# Copy application source code
COPY . .

# Build production bundle
RUN npm run build

# Stage 2: Serve via Nginx
FROM nginx:1.27-alpine AS runner

# Remove default configuration and static files
RUN rm -rf /usr/share/nginx/html/*

# Copy custom Nginx configuration with API reverse proxy
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy compiled browser assets from builder stage
COPY --from=builder /app/dist/webpick-initializr-frontend/browser /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
