# Demo Project focus on Redis, Spring WebFlux, (K8s), Docker

# Project Features

* ### Main Functions & APIs:
  1. Cat gathering sites at Taipei City(use u-bike sites currently at dev phase)
     * Google maps API
     * Redis Cache
     * Spring webflux
  2. Daily most visited spots ranking function
     * Redis 
  4. Chat Room function
     * Web-Socket
     * Spring webflux.

# Getting Started

* ###  using k8s(local)
  1. make sure you have kubectl & kind on your computer.
  2. start a cluster on your local machine, you can use below .yaml file
     ```
      kind: Cluster
      apiVersion: kind.x-k8s.io/v1alpha4
      name: dev-cluster
      nodes:
      - role: control-plane
        kubeadmConfigPatches:
        - |
          kind: InitConfiguration
          nodeRegistration:
            kubeletExtraArgs:
              node-labels: "ingress-ready=true"
        extraPortMappings:
        - containerPort: 80
          hostPort: 80
          protocol: TCP
        - containerPort: 31234
          hostPort: 31234
          protocol: TCP    
        - containerPort: 443
          hostPort: 443
          protocol: TCP
      - role: worker
      - role: worker    
      
      # kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml
     ```
  4. make sure to apply ```kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml``` because we're starting a ingress on local machine.
  5. cd to  file of k8s-local.
  6. open CMD or command shell of your computer and run below command to start the whole application set:
     ```
     kubectl apply -f .
     ```

* ### using docker-compose
  1. make sure you have installed docker & docker compose on your computer.
  2. cd to project's root path.
  3. open CMD or command shell of your computer and run below command to start the whole application set:
     ```
     docker compose --profile=app up
     ```
     or simply run below command if you plan to run the core application on local:
     ```
     docker compose up
     ```
