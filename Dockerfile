FROM mozilla/sbt

WORKDIR /app
COPY . /app
RUN \
  apt-get update && \
  apt-get install make -y && \
  apt-get clean
