#! /bin/bash

#echo "$#, $@"
if [ $# -eq "0" ]; then
  echo "Must provide a space delimited list of kubenetes object names."
else
  type="rolebinding"
  prefix="xxx-"
#  ns="default"

  for i in $@
  do
    kubectl delete -n $i $type $prefix$i;
  done
fi
