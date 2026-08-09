FROM php:8.2-cli-alpine

# Install system dependencies & PHP extensions
RUN apk add --no-cache git unzip bash postgresql-dev mysql-client && \
    docker-php-ext-install pdo pdo_mysql pdo_pgsql

# Install composer
COPY --from=composer:latest /usr/bin/composer /usr/bin/composer

WORKDIR /app

# Copy Composer files
COPY composer.json ./

# Install dependencies
RUN composer install --no-scripts --no-interaction --no-autoloader

# Copy application files
COPY . .

# Run autoloader
RUN composer dump-autoload --optimize

EXPOSE 8000

CMD ["php", "-S", "0.0.0.0:8000", "-t", "public/"]
