FROM mysql

RUN mkdir -p /sqlbackup

COPY ./sql /docker-entrypoint-initdb.d

#/docker-entrypoint-initdb.d 

EXPOSE 3306
