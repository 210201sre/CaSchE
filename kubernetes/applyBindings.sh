#! /bin/bash

#echo "$#, $@"

if [ $# -eq "0" ]; then
  echo "Must provide a space delimited list of namespaces."
else
  ManifestKind="RoleBinding"
  ManifestApiVersion="rbac.authorization.k8s.io/v1beta1"
  NamePrefix="xxx-"
  SubjectKind="Group"
  GroupName="chaos"
  RoleType="Role"
  RoleName="cluster-admin"

  for i in $@
  do
    echo "
kind: $ManifestKind
apiVersion: $ManifestApiVersion
metadata:
  name: $NamePrefix$i
  namespace: $i
subjects:
- kind: $SubjectKind
  name: $GroupName
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: $RoleType
  name: $RoleName
" | 
kubectl apply -n $i -f -;

  done
fi
