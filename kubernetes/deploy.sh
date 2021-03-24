#!/bin/bash

kubectl config use-context sre;
# Must have proper system variables to connect to database or must have identical database in RDS to run the below command.
# kubectl create -n casche secret generic casche-credentials --from-literal=url=$DB_URL --from-literal=username=$DB_USERNAME --from-literal=password=$DB_PASSWORD
kubectl create -n casche configmap fluent-conf --from-file fluent.conf
kubectl apply -n casche -f loki-external.yml
kubectl apply -n casche -f service-p2.yml
# kubectl apply -n casche -f ingress-p2.yml
kubectl apply -n casche -f deployment-p2.yml
kubectl apply -n casche -f svcmonitor.yml

kubectl -n casche rollout restart deployment

kubectl get -n casche all
kubectl get -n casche ing
kubectl get -n casche secrets
