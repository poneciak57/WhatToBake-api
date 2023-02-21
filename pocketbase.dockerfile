FROM alpine:3

ARG PB_VERSION=0.12.3
ARG ARCH=linux_amd64

RUN apk add --no-cache \
    unzip \
    ca-certificates

# download and unzip PocketBase
ADD https://github.com/pocketbase/pocketbase/releases/download/v${PB_VERSION}/pocketbase_${PB_VERSION}_${ARCH}.zip /tmp/pb.zip
RUN unzip /tmp/pb.zip -d /pb/

EXPOSE 9090

# start PocketBase
ENTRYPOINT ["/pb/pocketbase", "serve", "--http=0.0.0.0:9090"]