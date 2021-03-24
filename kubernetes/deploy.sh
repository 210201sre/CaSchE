#!/bin/bash
echo "Generating Logs"
echo ""
echo "Secrets" >> logs
kubectl describe -n casche secret casche-credentials >> logs
echo "Pods:" >> logs
kubectl describe -n casche pods >> logs
echo "Deployment:" >> logs
kubectl describe -n casche deployment project-two >> logs
echo "Service:" >> logs
kubectl describe -n casche service project-two >> logs
echo "Loki-External" >> logs
kubectl describe -n casche service loki >> logs
echo "Ingress" >> logs
kubectl describe -n casche ing project-two >> logs
echo "ServiceMonitor"
kubectl describe -n casche ServiceMonitor project-two >> logs
echo "=======================================================" >> logs
echo ""
echo "Reapplying some manifests"
kubectl config use-context sre;
# Must have proper system variables to connect to database or must have identical database in RDS to run the below command.
# kubectl create -n casche secret generic casche-credentials --from-literal=url=$DB_URL --from-literal=username=$DB_USERNAME --from-literal=password=$DB_PASSWORD
kubectl create -n casche configmap fluent-conf --from-file fluent.conf
kubectl apply -n casche -f loki-external.yml
kubectl apply -n casche -f service-p2.yml
# kubectl apply -n casche -f ingress-p2.yml
kubectl apply -n casche -f deployment-p2.yml
kubectl apply -n casche -f svcmonitor.yml
echo ""
echo "Restarting Deployment"
kubectl -n casche rollout restart deployment
echo ""
echo "Displaying Data"
kubectl get -n casche all
echo ""
echo "Displaying Unmodified Data"
kubectl get -n casche ing
kubectl get -n casche secrets
