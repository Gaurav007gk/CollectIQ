FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
ARG NEXT_PUBLIC_GOOGLE_CLIENT_ID=\nENV NEXT_PUBLIC_GOOGLE_CLIENT_ID=$NEXT_PUBLIC_GOOGLE_CLIENT_ID\nCOPY . .
RUN npm run build
FROM node:20-alpine
WORKDIR /app
ENV NODE_ENV=production
COPY --from=build /app ./
EXPOSE 3000
CMD ["npm","start"]