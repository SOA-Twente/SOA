kubectl config set-context --current --namespace=production

FOR /F "delims= " %%i IN ('kubectl get pods ^| findstr db-follow') DO set podname=%%i
kubectl exec --stdin --tty %podname% -- /bin/bash -c "PGPASSWORD=root psql -h db-follow -U postgres db_follow -c 'SELECT * FROM followings;'"

FOR /F "delims= " %%i IN ('kubectl get pods ^| findstr db-register') DO set podname=%%i
kubectl exec --stdin --tty %podname% -- /bin/bash -c "PGPASSWORD=root psql -h db-register -U postgres db_register -c 'SELECT * FROM users;'"

FOR /F "delims= " %%i IN ('kubectl get pods ^| findstr db-post-message') DO set podname=%%i
kubectl exec --stdin --tty %podname% -- /bin/bash -c "PGPASSWORD=root psql -h db-post-message -U postgres db_post_message -c 'SELECT * FROM quacks;'"

FOR /F "delims= " %%i IN ('kubectl get pods ^| findstr db-profile') DO set podname=%%i
kubectl exec --stdin --tty %podname% -- /bin/bash -c "PGPASSWORD=root psql -h db-profile -U postgres db_profile -c 'SELECT * FROM userdata;'"

