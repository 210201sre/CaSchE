#!/bin/bash
echo "Generating Logs"
echo ""
echo "Secrets:" >> logs
kubectl describe -n casche secret casche-credentials >> logs
echo "Pods:" >> logs
kubectl describe -n casche pods >> logs
echo "Deployment:" >> logs
kubectl describe -n casche deployment project-two >> logs
echo "Service:" >> logs
kubectl describe -n casche service project-two >> logs
echo "Loki-External:" >> logs
kubectl describe -n casche service loki >> logs
echo "Ingress:" >> logs
kubectl describe -n casche ing project-two >> logs
echo "ServiceMonitor:" >> logs
kubectl describe -n casche ServiceMonitor project-two >> logs
echo "=======================================================" >> logs
echo "Reapplying some manifests"
echo ""
kubectl config use-context sre;
# Must have proper system variables to connect to database or must have identical database in RDS to run the below command.
# Comment the line below after the system is set up.
 kubectl create -n casche secret generic casche-credentials --from-literal=url=$DB_URL --from-literal=username=$DB_USERNAME --from-literal=password=$DB_PASSWORD
echo "=======================================================" >> err
kubectl create -n casche configmap fluent-conf --from-file fluent.conf >> err
kubectl apply -n casche -f loki-external.yml >> err
kubectl apply -n casche -f service-p2.yml >> err
# Comment the line below after the system is set up.
 kubectl apply -n casche -f ingress-p2.yml >> err
kubectl apply -n casche -f deployment-p2.yml >> err
kubectl apply -n casche -f svcmonitor.yml >> err
echo ""
echo "Restarting Deployment"
kubectl -n casche rollout restart deployment
kubectl delete -n casche replicasets --all
echo ""
echo "Displaying Data"
kubectl get -n casche all
echo ""
echo "Displaying Unmodified Data"
kubectl get -n casche ing
kubectl get -n casche secrets
kubectl get -n casche configmap

