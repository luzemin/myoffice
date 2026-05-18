# =====================================================================
# Multi-stage build for the MyOffice SvelteKit frontend.
# Stage 1: install deps and produce the static SPA build.
# Stage 2: serve static files + reverse-proxy API / editor through nginx.
# =====================================================================

# ---- Stage 1: build ----
FROM node:20-alpine AS build

WORKDIR /workspace

COPY package.json ./
# package-lock.json is optional; copy if present.
COPY package-lock.json* ./

RUN npm install --no-audit --no-fund

COPY . .

RUN npm run build

# ---- Stage 2: nginx ----
FROM nginx:1.27-alpine

# Replace default nginx config with our SPA + reverse-proxy config.
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy SvelteKit static-adapter output.
COPY --from=build /workspace/build /usr/share/nginx/html

EXPOSE 80
