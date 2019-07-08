FROM mozilla/sbt

WORKDIR /app
COPY . /app

ENV CASS_HOST=cassandra

RUN \
  apt-get update && \
  apt-get install make -y && \
  apt-get clean
