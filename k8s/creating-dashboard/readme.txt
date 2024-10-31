all the files in this folder are used to create a user to access the kubernetes dashboard. 
to create a kubernetes dashboard we need to follow the installation procedure mentioned in 
the below documentation. 
https://kubernetes.io/docs/tasks/access-application-cluster/web-ui-dashboard/ -->  basically we created a cluster using docker desktop
and created a kubernetes dashboard using helm. 

once the containers related to the kubernetes dashboard are downloaded we are supposed to create a sample user inorder to acess
the dashboard. 
all the files in this folder are used to create a user, map a role to him and create a secret for him so that we can access the 
dashboard unlimited times. follow below link to understand more. 
https://github.com/kubernetes/dashboard/blob/master/docs/user/access-control/creating-sample-user.md
