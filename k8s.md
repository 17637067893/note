#### 服务器

harbor服务

```
host 192.168.111.156
name           admin
password      Harbor12345
```

mysql

```
host 192.168.111.156
name           root
password      root
```



#### 概述

k8s一种容器化集群技术

######  功能

1. 自动装箱

   基于容器对应用运行环境的资源配置要求自动部署应用容器

2. 自我修复 ( 自愈能力) 

   当容器失败时，会对容器进行重启
   当所部署的 Node 节点有问题时，会对容器进行重新部署和重新调度
   当容器未通过监控检查时，会关闭此容器直到容器正常运行时，才会对外提供服务 

3. 服务发现

   用户不需使用额外的服务发现机制，就能够基于 Kubernetes 自身能力实现服务发现和
   负载均衡

#### docker深入

###### 1 概述

在 Docker 中创建镜像最常用的方式，就是使用 Dockerfile。Dockerfile 是一个 Docker 镜像
的描述文件，我们可以理解成火箭发射的 A、B、C、D…的步骤。Dockerfile 其内部包含了一
条条的指令，每一条指令构建一层，因此每一条指令的内容，就是描述该层应当如何构建。

###### 2 示例

```
#基于 centos 镜像
FROM centos

#维护人的信息
MAINTAINER My CentOS <534096094@qq.com>

#安装 httpd 软件包
RUN yum -y update
RUN yum -y install httpd

#开启 80 端口
EXPOSE 80

#复制网站首页文件至镜像中 web 站点下
ADD index.html /var/www/html/index.htm

#复制该脚本至镜像中，并修改其权限
ADD run.sh /run.sh
RUN chmod 775 /run.sh

#当启动容器时执行的脚本文件
CMD ["/run.sh"]
```

![image-20210505231132109](G:\note\image\image-20210505231132109.png)

###### 3 镜像操作

1. 创建项目dockerfile文件

2. 上传项目到服务器。

3. 进入项目，构建镜像到本地仓库；

   (1) docker build -t nginx:GA-1.0 -f ./Dockerfile . 别忘了最后的小数点。

   (2) docker images 查看镜像

   (3) docker exec -it 容器 id /bin/bash；进入容器，修改容器

   (4) docker commit -a “leifengyang” -m “nginxxx” 容器 id mynginx:GA-2.0

        ```
   docker commit [OPTIONS] CONTAINER [REPOSITORY[:TAG]]
       1) -a :提交的镜像作者；
       2) -c :使用 Dockerfile 指令来创建镜像；
       3) -m :提交时的说明文字；
       4) -p :在 commit 时，将容器暂停。
   
        ```

   (5) docker login : 登陆到一个 Docker 镜像仓库，如果未指定镜像仓库地址，默认为官
   方仓库 Docker Hub
   1 docker login -u 用户名 -p 密码

   (6) docker logout : 登出一个 Docker 镜像仓库，如果未指定镜像仓库地址，默认为官
   方仓库 Docker Hub

   

   4、推送镜像到 docker hub

   (1) 标记镜像，docker tag local-image:tagname username/new-repo:tagname

   (2) 上传镜像，docker push username/new-repo:tagname

   

   5、保存镜像，加载镜像

   (1) 可以保存镜像为 tar，使用 u 盘等设备复制到任意 docker 主机，再次加载镜像
   (2) 保存：docker save spring-boot-docker -o /home/spring-boot-docker.tar
   (3) 加载：docker load -i spring-boot-docker.tar

   

   6、阿里云操作
   (1) 登录阿里云，密码就是开通镜像仓库时 的密码
   docker login --username=qwertyuiopasdf_aa registry.cn-hangzhou.aliyuncs.com
   (2) 拉取镜像
   docker pull registry.cn-hangzhou.aliyuncs.com/atguigumall/gulimall-nginx:v1.0
   (3）推送镜像
   docker tag [ImageId] registry.cn-hangzhou.aliyuncs.com/atguigumall/gulimall-nginx:v1
   docker push registry.cn-hangzhou.aliyuncs.com/atguigumall/gulimall-nginx:v1

#### k8s集群架构

![image-20210429155022468](G:\note\image\image-20210429155022468.png)

![03-第一部分 核心概念](G:\note\image\03-第一部分 核心概念.png)![image-20210429155134517](G:\note\image\image-20210429155134517.png)

###### 单master和多master

![image-20210429160643028](G:\note\image\image-20210429160643028.png)

#### kubeadm搭建集群

###### 1 准备三台服务器

一台或多台机器，操作系统 CentOS7.x-86_x64
- 硬件配置：2GB 或更多 RAM，2 个 CPU 或更多 CPU，硬盘 30GB 或更多

- 集群中所有机器之间网络互通

- 可以访问外网，需要拉取镜像

- 禁止 swap 分区

  ```
  swapoff -a 临时
  sed -ri 's/.*swap.*/#&/' /etc/fstab 永久
  free -g 验证，swap 必须为 0；
  ```

  

1. 准备工作

   ```
   # 关闭防火墙
   systemctl stop firewalld
   systemctl disable firewalld
   
   # 关闭selinux
   sed -i 's/enforcing/disabled/' /etc/selinux/config  # 永久
   setenforce 0  # 临时
   
   # 关闭swap
   swapoff -a  # 临时
   sed -ri 's/.*swap.*/#&/' /etc/fstab    # 永久
   
   # 根据规划设置主机名
   hostnamectl set-hostname <hostname>
   
   # 在master添加hosts
   cat >> /etc/hosts << EOF
   192.168.111.139 master
   192.168.111.143 node1
   192.168.111.144 node1
   EOF
   
   # 将桥接的IPv4流量传递到iptables的链
   cat > /etc/sysctl.d/k8s.conf << EOF
   net.bridge.bridge-nf-call-ip6tables = 1
   net.bridge.bridge-nf-call-iptables = 1
   EOF
   sysctl --system  # 生效
   
   # 时间同步
   yum install ntpdate -y
   ntpdate time.windows.com
   ```

   

2. 所有节点安装Docker/kubeadm/kubelet

   ```
   安装docker
   $ wget https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo -O /etc/yum.repos.d/docker-ce.repo
   $ yum -y install docker-ce-18.06.1.ce-3.el7
   $ systemctl enable docker && systemctl start docker
   $ docker --version
   Docker version 18.06.1-ce, build e68fc7a
   
   
   修改仓库地址
   sudo mkdir -p /etc/docker
   sudo tee /etc/docker/daemon.json <<-'EOF'
   {
     "registry-mirrors": ["https://64dnk2wc.mirror.aliyuncs.com"]
   }
   EOF
   sudo systemctl daemon-reload
   sudo systemctl restart docker
   
   添加阿里云YUM软件源
   cat > /etc/yum.repos.d/kubernetes.repo << EOF
   [kubernetes]
   name=Kubernetes
   baseurl=https://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-x86_64
   enabled=1
   gpgcheck=0
   repo_gpgcheck=0
   gpgkey=https://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg https://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
   EOF
   ```

   

3. 安装kubeadm，kubelet和kubectl

   ```
   yum install -y kubelet-1.18.0 kubeadm-1.18.0 kubectl-1.18.0
    systemctl enable kubelet
   ```

   

###### 2  部署 Master

在master上执行

```
kubeadm init \
  --apiserver-advertise-address=192.168.111.139 \
  --image-repository registry.aliyuncs.com/google_containers \
  --kubernetes-version v1.18.0 \
  --service-cidr=10.96.0.0/12 \
  --pod-network-cidr=10.244.0.0/16
```

完成后执行代码

![image-20210429163949993](G:\note\image\image-20210429163949993.png)

```
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
```

在其他node节点执行

```
kubeadm join 192.168.111.139:6443 --token 12xizb.pghrcr92a0pdwpdu \
    --discovery-token-ca-cert-hash sha256:506493892604fb7f39f638a4ccb37340cb97b823a9ee490b4df35587ca8eab32
```

默认token有效期为24小时，当过期之后，该token就不可用了。这时就需要重新创建token，操作如下：

```
kubeadm token create --print-join-command
```

在master执行 kubectl get nodes 可以看到添加的节点

![image-20210429164333647](G:\note\image\image-20210429164333647.png)

1 删除节点

```
1  删除一个节点前，先驱赶掉上面的pod
kubectl drain node-06 --delete-local-data --force --ignore-daemonsets
2 恢复驱赶的节点
kubectl uncordon <node_name>
```

检查节点状态，被标记为不可调度节点

![image-20210429174859598](G:\note\image\image-20210429174859598.png)

2 然后删除

```
kubectl delete node k8snode2
```



###### 3 部署CNI网络插件

由于缺少 kube-flannel.yml 显示为NotReady状态

默认镜像地址无法访问，sed命令修改为docker hub镜像仓库。

```
kubectl apply -f https://github.com/mrlxxx/kube-flannel.yml/blob/master/kube-flannel.yml
```

可以使用  watch kubectl get pod -n kube-system -o wide 监控 pod 进度等网络初始化完成后在master上 kubectl  get nodes 查看节点都为ready状态

![image-20210501101934321](G:\note\image\image-20210501101934321.png)



自己手动生成kube-flannel.yml

```
---
apiVersion: policy/v1beta1
kind: PodSecurityPolicy
metadata:
  name: psp.flannel.unprivileged
  annotations:
    seccomp.security.alpha.kubernetes.io/allowedProfileNames: docker/default
    seccomp.security.alpha.kubernetes.io/defaultProfileName: docker/default
    apparmor.security.beta.kubernetes.io/allowedProfileNames: runtime/default
    apparmor.security.beta.kubernetes.io/defaultProfileName: runtime/default
spec:
  privileged: false
  volumes:
    - configMap
    - secret
    - emptyDir
    - hostPath
  allowedHostPaths:
    - pathPrefix: "/etc/cni/net.d"
    - pathPrefix: "/etc/kube-flannel"
    - pathPrefix: "/run/flannel"
  readOnlyRootFilesystem: false
  # Users and groups
  runAsUser:
    rule: RunAsAny
  supplementalGroups:
    rule: RunAsAny
  fsGroup:
    rule: RunAsAny
  # Privilege Escalation
  allowPrivilegeEscalation: false
  defaultAllowPrivilegeEscalation: false
  # Capabilities
  allowedCapabilities: ['NET_ADMIN']
  defaultAddCapabilities: []
  requiredDropCapabilities: []
  # Host namespaces
  hostPID: false
  hostIPC: false
  hostNetwork: true
  hostPorts:
  - min: 0
    max: 65535
  # SELinux
  seLinux:
    # SELinux is unused in CaaSP
    rule: 'RunAsAny'
---
kind: ClusterRole
apiVersion: rbac.authorization.k8s.io/v1beta1
metadata:
  name: flannel
rules:
  - apiGroups: ['extensions']
    resources: ['podsecuritypolicies']
    verbs: ['use']
    resourceNames: ['psp.flannel.unprivileged']
  - apiGroups:
      - ""
    resources:
      - pods
    verbs:
      - get
  - apiGroups:
      - ""
    resources:
      - nodes
    verbs:
      - list
      - watch
  - apiGroups:
      - ""
    resources:
      - nodes/status
    verbs:
      - patch
---
kind: ClusterRoleBinding
apiVersion: rbac.authorization.k8s.io/v1beta1
metadata:
  name: flannel
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: flannel
subjects:
- kind: ServiceAccount
  name: flannel
  namespace: kube-system
---
apiVersion: v1
kind: ServiceAccount
metadata:
  name: flannel
  namespace: kube-system
---
kind: ConfigMap
apiVersion: v1
metadata:
  name: kube-flannel-cfg
  namespace: kube-system
  labels:
    tier: node
    app: flannel
data:
  cni-conf.json: |
    {
      "name": "cbr0",
      "cniVersion": "0.3.1",
      "plugins": [
        {
          "type": "flannel",
          "delegate": {
            "hairpinMode": true,
            "isDefaultGateway": true
          }
        },
        {
          "type": "portmap",
          "capabilities": {
            "portMappings": true
          }
        }
      ]
    }
  net-conf.json: |
    {
      "Network": "10.244.0.0/16",
      "Backend": {
        "Type": "vxlan"
      }
    }
---
apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: kube-flannel-ds-amd64
  namespace: kube-system
  labels:
    tier: node
    app: flannel
spec:
  selector:
    matchLabels:
      app: flannel
  template:
    metadata:
      labels:
        tier: node
        app: flannel
    spec:
      affinity:
        nodeAffinity:
          requiredDuringSchedulingIgnoredDuringExecution:
            nodeSelectorTerms:
              - matchExpressions:
                  - key: beta.kubernetes.io/os
                    operator: In
                    values:
                      - linux
                  - key: beta.kubernetes.io/arch
                    operator: In
                    values:
                      - amd64
      hostNetwork: true
      tolerations:
      - operator: Exists
        effect: NoSchedule
      serviceAccountName: flannel
      initContainers:
      - name: install-cni
        image: easzlab/flannel:v0.13.0-amd64
        command:
        - cp
        args:
        - -f
        - /etc/kube-flannel/cni-conf.json
        - /etc/cni/net.d/10-flannel.conflist
        volumeMounts:
        - name: cni
          mountPath: /etc/cni/net.d
        - name: flannel-cfg
          mountPath: /etc/kube-flannel/
      containers:
      - name: kube-flannel
        image: easzlab/flannel:v0.13.0-amd64
        command:
        - /opt/bin/flanneld
        args:
        - --ip-masq
        - --kube-subnet-mgr
        resources:
          requests:
            cpu: "100m"
            memory: "50Mi"
          limits:
            cpu: "100m"
            memory: "50Mi"
        securityContext:
          privileged: false
          capabilities:
            add: ["NET_ADMIN"]
        env:
        - name: POD_NAME
          valueFrom:
            fieldRef:
              fieldPath: metadata.name
        - name: POD_NAMESPACE
          valueFrom:
            fieldRef:
              fieldPath: metadata.namespace
        volumeMounts:
        - name: run
          mountPath: /run/flannel
        - name: flannel-cfg
          mountPath: /etc/kube-flannel/
      volumes:
        - name: run
          hostPath:
            path: /run/flannel
        - name: cni
          hostPath:
            path: /etc/cni/net.d
        - name: flannel-cfg
          configMap:
            name: kube-flannel-cfg
---
apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: kube-flannel-ds-arm64
  namespace: kube-system
  labels:
    tier: node
    app: flannel
spec:
  selector:
    matchLabels:
      app: flannel
  template:
    metadata:
      labels:
        tier: node
        app: flannel
    spec:
      affinity:
        nodeAffinity:
          requiredDuringSchedulingIgnoredDuringExecution:
            nodeSelectorTerms:
              - matchExpressions:
                  - key: beta.kubernetes.io/os
                    operator: In
                    values:
                      - linux
                  - key: beta.kubernetes.io/arch
                    operator: In
                    values:
                      - arm64
      hostNetwork: true
      tolerations:
      - operator: Exists
        effect: NoSchedule
      serviceAccountName: flannel
      initContainers:
      - name: install-cni
        image: quay.io/coreos/flannel:v0.11.0-arm64
        command:
        - cp
        args:
        - -f
        - /etc/kube-flannel/cni-conf.json
        - /etc/cni/net.d/10-flannel.conflist
        volumeMounts:
        - name: cni
          mountPath: /etc/cni/net.d
        - name: flannel-cfg
          mountPath: /etc/kube-flannel/
      containers:
      - name: kube-flannel
        image: quay.io/coreos/flannel:v0.11.0-arm64
        command:
        - /opt/bin/flanneld
        args:
        - --ip-masq
        - --kube-subnet-mgr
        resources:
          requests:
            cpu: "100m"
            memory: "50Mi"
          limits:
            cpu: "100m"
            memory: "50Mi"
        securityContext:
          privileged: false
          capabilities:
             add: ["NET_ADMIN"]
        env:
        - name: POD_NAME
          valueFrom:
            fieldRef:
              fieldPath: metadata.name
        - name: POD_NAMESPACE
          valueFrom:
            fieldRef:
              fieldPath: metadata.namespace
        volumeMounts:
        - name: run
          mountPath: /run/flannel
        - name: flannel-cfg
          mountPath: /etc/kube-flannel/
      volumes:
        - name: run
          hostPath:
            path: /run/flannel
        - name: cni
          hostPath:
            path: /etc/cni/net.d
        - name: flannel-cfg
          configMap:
            name: kube-flannel-cfg
---
apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: kube-flannel-ds-arm
  namespace: kube-system
  labels:
    tier: node
    app: flannel
spec:
  selector:
    matchLabels:
      app: flannel
  template:
    metadata:
      labels:
        tier: node
        app: flannel
    spec:
      affinity:
        nodeAffinity:
          requiredDuringSchedulingIgnoredDuringExecution:
            nodeSelectorTerms:
              - matchExpressions:
                  - key: beta.kubernetes.io/os
                    operator: In
                    values:
                      - linux
                  - key: beta.kubernetes.io/arch
                    operator: In
                    values:
                      - arm
      hostNetwork: true
      tolerations:
      - operator: Exists
        effect: NoSchedule
      serviceAccountName: flannel
      initContainers:
      - name: install-cni
        image: quay.io/coreos/flannel:v0.11.0-arm
        command:
        - cp
        args:
        - -f
        - /etc/kube-flannel/cni-conf.json
        - /etc/cni/net.d/10-flannel.conflist
        volumeMounts:
        - name: cni
          mountPath: /etc/cni/net.d
        - name: flannel-cfg
          mountPath: /etc/kube-flannel/
      containers:
      - name: kube-flannel
        image: quay.io/coreos/flannel:v0.11.0-arm
        command:
        - /opt/bin/flanneld
        args:
        - --ip-masq
        - --kube-subnet-mgr
        resources:
          requests:
            cpu: "100m"
            memory: "50Mi"
          limits:
            cpu: "100m"
            memory: "50Mi"
        securityContext:
          privileged: false
          capabilities:
             add: ["NET_ADMIN"]
        env:
        - name: POD_NAME
          valueFrom:
            fieldRef:
              fieldPath: metadata.name
        - name: POD_NAMESPACE
          valueFrom:
            fieldRef:
              fieldPath: metadata.namespace
        volumeMounts:
        - name: run
          mountPath: /run/flannel
        - name: flannel-cfg
          mountPath: /etc/kube-flannel/
      volumes:
        - name: run
          hostPath:
            path: /run/flannel
        - name: cni
          hostPath:
            path: /etc/cni/net.d
        - name: flannel-cfg
          configMap:
            name: kube-flannel-cfg
---
apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: kube-flannel-ds-ppc64le
  namespace: kube-system
  labels:
    tier: node
    app: flannel
spec:
  selector:
    matchLabels:
      app: flannel
  template:
    metadata:
      labels:
        tier: node
        app: flannel
    spec:
      affinity:
        nodeAffinity:
          requiredDuringSchedulingIgnoredDuringExecution:
            nodeSelectorTerms:
              - matchExpressions:
                  - key: beta.kubernetes.io/os
                    operator: In
                    values:
                      - linux
                  - key: beta.kubernetes.io/arch
                    operator: In
                    values:
                      - ppc64le
      hostNetwork: true
      tolerations:
      - operator: Exists
        effect: NoSchedule
      serviceAccountName: flannel
      initContainers:
      - name: install-cni
        image: quay.io/coreos/flannel:v0.11.0-ppc64le
        command:
        - cp
        args:
        - -f
        - /etc/kube-flannel/cni-conf.json
        - /etc/cni/net.d/10-flannel.conflist
        volumeMounts:
        - name: cni
          mountPath: /etc/cni/net.d
        - name: flannel-cfg
          mountPath: /etc/kube-flannel/
      containers:
      - name: kube-flannel
        image: quay.io/coreos/flannel:v0.11.0-ppc64le
        command:
        - /opt/bin/flanneld
        args:
        - --ip-masq
        - --kube-subnet-mgr
        resources:
          requests:
            cpu: "100m"
            memory: "50Mi"
          limits:
            cpu: "100m"
            memory: "50Mi"
        securityContext:
          privileged: false
          capabilities:
             add: ["NET_ADMIN"]
        env:
        - name: POD_NAME
          valueFrom:
            fieldRef:
              fieldPath: metadata.name
        - name: POD_NAMESPACE
          valueFrom:
            fieldRef:
              fieldPath: metadata.namespace
        volumeMounts:
        - name: run
          mountPath: /run/flannel
        - name: flannel-cfg
          mountPath: /etc/kube-flannel/
      volumes:
        - name: run
          hostPath:
            path: /run/flannel
        - name: cni
          hostPath:
            path: /etc/cni/net.d
        - name: flannel-cfg
          configMap:
            name: kube-flannel-cfg
---
apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: kube-flannel-ds-s390x
  namespace: kube-system
  labels:
    tier: node
    app: flannel
spec:
  selector:
    matchLabels:
      app: flannel
  template:
    metadata:
      labels:
        tier: node
        app: flannel
    spec:
      affinity:
        nodeAffinity:
          requiredDuringSchedulingIgnoredDuringExecution:
            nodeSelectorTerms:
              - matchExpressions:
                  - key: beta.kubernetes.io/os
                    operator: In
                    values:
                      - linux
                  - key: beta.kubernetes.io/arch
                    operator: In
                    values:
                      - s390x
      hostNetwork: true
      tolerations:
      - operator: Exists
        effect: NoSchedule
      serviceAccountName: flannel
      initContainers:
      - name: install-cni
        image: quay.io/coreos/flannel:v0.11.0-s390x
        command:
        - cp
        args:
        - -f
        - /etc/kube-flannel/cni-conf.json
        - /etc/cni/net.d/10-flannel.conflist
        volumeMounts:
        - name: cni
          mountPath: /etc/cni/net.d
        - name: flannel-cfg
          mountPath: /etc/kube-flannel/
      containers:
      - name: kube-flannel
        image: quay.io/coreos/flannel:v0.11.0-s390x
        command:
        - /opt/bin/flanneld
        args:
        - --ip-masq
        - --kube-subnet-mgr
        resources:
          requests:
            cpu: "100m"
            memory: "50Mi"
          limits:
            cpu: "100m"
            memory: "50Mi"
        securityContext:
          privileged: false
          capabilities:
             add: ["NET_ADMIN"]
        env:
        - name: POD_NAME
          valueFrom:
            fieldRef:
              fieldPath: metadata.name
        - name: POD_NAMESPACE
          valueFrom:
            fieldRef:
              fieldPath: metadata.namespace
        volumeMounts:
        - name: run
          mountPath: /run/flannel
        - name: flannel-cfg
          mountPath: /etc/kube-flannel/
      volumes:
        - name: run
          hostPath:
            path: /run/flannel
        - name: cni
          hostPath:
            path: /etc/cni/net.d
        - name: flannel-cfg
          configMap:
            name: kube-flannel-cfg
```

生成文件后执行

```
kubectl apply -f kube-flannel.yml 
```

可以使用kubectl get pods -n kube-system查看pod运行状态

所有为Runing时

![image-20210501100036551](G:\note\image\image-20210501100036551.png)

使用kubect get nodes查看节点为Ready状态

![image-20210501100138083](G:\note\image\image-20210501100138083.png)

###### 4 测试kubernetes集群

```
在master拉取ngix
$ kubectl create deployment nginx --image=nginx
使用 kubectl get pod 查看nginx状态
对外暴露端口
$ kubectl expose deployment nginx --port=80 --type=NodePort

kubectl expose deployment tomcat6 --port=80 --target-port=8080 --type=NodePort
Pod 的 80 映射容器的 8080；service 会代理 Pod 的 80
```



查看对外映射的端口号

![image-20210429180627784](G:\note\image\image-20210429180627784.png)

通过**node节点ip**加对外的端口号就能访问nginx

```
http://192.168.111.145:32510/
```

![image-20210429180731578](G:\note\image\image-20210429180731578.png)

动态扩容测试

```
kubectl get deployment
应用升级 kubectl set image (--help 查看帮助)
扩容： kubectl scale --replicas=3 deployment tomcat6
```





#### 二进制搭建kubeadmin集群

###### 1  安装要求

在开始之前，部署 Kubernetes 集群机器需要满足以下几个条件：
（1）一台或多台机器，操作系统 CentOS7.x-86_x64
（2）硬件配置：2GB 或更多 RAM，2 个 CPU 或更多 CPU，硬盘 30GB 或更多
（3）集群中所有机器之间网络互通
（4）可以访问外网，需要拉取镜像，如果服务器不能上网，需要提前下载镜像并导入节点
（5）禁止 swap 分区

###### 2 系统初始化

```
# 关闭防火墙
systemctl stop firewalld
systemctl disable firewalld

# 关闭selinux
sed -i 's/enforcing/disabled/' /etc/selinux/config  # 永久
setenforce 0  # 临时

# 关闭swap
swapoff -a  # 临时
sed -ri 's/.*swap.*/#&/' /etc/fstab    # 永久

# 根据规划设置主机名
hostnamectl set-hostname <hostname>

# 在master添加hosts
cat >> /etc/hosts << EOF
192.168.111.139 k8smaster
192.168.111.143 k8snode1
192.168.111.144 k8snode2
EOF

# 将桥接的IPv4流量传递到iptables的链
cat > /etc/sysctl.d/k8s.conf << EOF
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
EOF
sysctl --system  # 生效

# 时间同步
yum install ntpdate -y
ntpdate time.windows.com
```

###### 3 生成证书

1 cfssl 是一个开源的证书管理工具，使用 json 文件生成证书，相比 openssl 更方便使用。
找任意一台服务器操作，这里用 Master 节点。

```
wget https://pkg.cfssl.org/R1.2/cfssl_linux-amd64
wget https://pkg.cfssl.org/R1.2/cfssljson_linux-amd64
wget https://pkg.cfssl.org/R1.2/cfssl-certinfo_linux-amd64
chmod +x cfssl_linux-amd64 cfssljson_linux-amd64 cfssl-certinfo_linux-amd64
mv cfssl_linux-amd64 /usr/local/bin/cfssl
mv cfssljson_linux-amd64 /usr/local/bin/cfssljson
mv cfssl-certinfo_linux-amd64 /usr/local/bin/cfssl-certinfo
```

2 进入到  /usr/local/bin/执行

```json
cat > ca-config.json<< EOF
{
    "signing": {
        "default": {
            "expiry": "87600h"
        },
        "profiles": {
            "www": {
                "expiry": "87600h",
                "usages": [
                    "signing",
                    "key encipherment",
                    "server auth",
                    "client auth"
                ]
            }
        }
    }
}
EOF
cat > ca-csr.json<< EOF
{
    "CN": "etcd CA",
    "key": {
        "algo": "rsa",
        "size": 2048
    },
    "names": [
        {
            "C": "CN",
            "L": "Beijing",
            "ST": "Beijing"
        }
    ]
}
EOF
```

生成证书

```
cfssl gencert -initca ca-csr.json | cfssljson -bare ca -
```

![image-20210429193930199](G:\note\image\image-20210429193930199.png)

```
执行 ls *pem
ca-key.pem ca.pem
```

使用自签 CA 签发 Etcd HTTPS 证书

创建证书申请文件：

```json
cat > server-csr.json<< EOF
{
    "CN": "etcd",
    "hosts": [
        "192.168.111.146",
        "192.168.111.147"
    ],
    "key": {
        "algo": "rsa",
        "size": 2048
    },
    "names": [
        {
            "C": "CN",
            "L": "BeiJing",
            "ST": "BeiJing"
        }
    ]
}
EOF
```

执行命令 生成证书文件

```
fssl gencert -ca=ca.pem -ca-key=ca-key.pem -config=ca-config.json -profile=www server-csr.json | cfssljson -bare server
```

![image-20210429194615593](G:\note\image\image-20210429194615593.png)

#### kubernetes 集群命令行工具 kubectl

kubectl 是 是 s Kubernetes 集群的命令行工具，通过 kubectl 能够对集群本身进行管理，并能
够在集群上进行容器化应用的安装部署。

用法

```
kubectl [command] [TYPE] [name] [flags]
command :对资源的操作  create get describe delete
TYPE 执行资源类型，资源类型是大小写敏感
例如
kubectl get pod pod1
kubectl get pods pod1
kubectl get po pod1

name 指定资源的名称大小写敏感 如省略会显示所有资源
kubectl get pods

flags：指定可选的参数。例如，可用-s 或者–server 参数指定 Kubernetes API
server 的地址和端口。
```

#### kubernetes 集群 L YAML 文件详解

快速生成yaml

![image-20210429215908367](G:\note\image\image-20210429215908367.png)

1 命令行生成

```
1 kubectl create deployment web --image=nginx -o yaml --dry-run > myl.yaml
--dry-run 并不执行yaml
myl.yaml 生成到myl.yaml文件
```

2 kubectl get 命令导出yaml

```
kubectl get deploy  nginx -o=yaml --export > my2.yaml
```

#### 核心 Pod

Pod 是 k8s 系统中可以创建和管理的最小单元

###### 共享网络![11-1-Pod实现机制-共享网络](G:\note\image\11-1-Pod实现机制-共享网络.png)

###### 共享存储

![image-20210429221846184](G:\note\image\image-20210429221846184.png)

###### 镜像拉取策略

![image-20210429222230645](G:\note\image\image-20210429222230645.png)

###### 资源限制

![image-20210429222706162](G:\note\image\image-20210429222706162.png)

###### 重启策略

![image-20210429222853683](G:\note\image\image-20210429222853683.png)

###### 健康检查

![15-Pod健康检查](G:\note\image\15-Pod健康检查.png)

###### 节点调度

![image-20210429235008122](G:\note\image\image-20210429235008122.png)

###### 节点亲和性

![image-20210429235354090](G:\note\image\image-20210429235354090.png)

###### 污点调用

![image-20210430001149760](G:\note\image\image-20210430001149760.png)

![image-20210430001255668](G:\note\image\image-20210430001255668.png)

1 给node1节点添加污点

```
kubectl taint node k8snode1 env_role=yes:NoSchedule
删除污点
kubectl taint node k8snode1 env_role:NoSchedule-
```

2 查看污点

```
kubectl describe node k8snode1 | grep Taint
```

![image-20210430001641269](G:\note\image\image-20210430001641269.png)

3 创建

```
kubectl create deployment webs --image=nginx

kubectl scale deployment webs --replicas=5
```

4 查看

```
 kubectl get pods -o wide
```

全部被分配到node2节点

![image-20210430001829936](G:\note\image\image-20210430001829936.png)





###### 总截图

![11-Pod](G:\note\image\11-Pod.png)

###### Pod创建流程

![image-20210429234126899](G:\note\image\image-20210429234126899.png)

#### 核心Controller

###### 无状态部署

部署容器

![19-Controller控制器（deployment）](G:\note\image\19-Controller控制器（deployment）.png)

###### 有状态部署

```
apiVersion: v1
kind: Service
metadata:
  name: nginx
  labels:
    app: nginx
spec:
  ports:
  - port: 80
    name: web
  clusterIP: None
  selector:
    app: nginx

---

apiVersion: apps/v1
kind: StatefulSet #有状态
metadata:
  name: nginx-statefulset
  namespace: default
spec:
  serviceName: nginx
  replicas: 3
  selector:
    matchLabels:
      app: nginx
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
      - name: nginx
        image: nginx:latest
        ports:
        - containerPort: 80

```



![image-20210430103519489](G:\note\image\image-20210430103519489.png)

###### 守护进程

![image-20210430105517237](G:\note\image\image-20210430105517237.png)

```
apiVersion: apps/v1
kind: DaemonSet #守护进程
metadata:
  name: ds-test  #日志采集
  labels:
    app: filebeat
spec:
  selector:
    matchLabels:
      app: filebeat
  template:
    metadata:
      labels:
        app: filebeat
    spec:
      containers:
      - name: logs
        image: nginx
        ports:
        - containerPort: 80
        volumeMounts:
        - name: varlog
          mountPath: /tmp/log
      volumes:
      - name: varlog
        hostPath:
          path: /var/log

```

执行yaml文件

```
kubectl apply -f ds.yaml
```

![image-20210430105536916](G:\note\image\image-20210430105536916.png)

创建好容器后

```
kubectl exec -it ds-test-b7xxr bash 进入容器

查看日志内容
ls /tmp/log
```

![image-20210430105706086](G:\note\image\image-20210430105706086.png)

###### 一次任务

1 根据yaml

```
apiVersion: batch/v1
kind: Job
metadata:
  name: pi
spec:
  template:
    spec:
      containers:
      - name: pi
        image: perl
        command: ["perl",  "-Mbignum=bpi", "-wle", "print bpi(2000)"]
      restartPolicy: Never
  backoffLimit: 4

```

2 执行yaml文件  

```
kubectl apply -f job.yaml
```

![image-20210430110930865](G:\note\image\image-20210430110930865.png)

查看job

```
kubectl get jobs
```

![image-20210430111033434](G:\note\image\image-20210430111033434.png)

3 查看日志

```
kubectl logs pi-242g8
```

![image-20210430111105630](G:\note\image\image-20210430111105630.png)

4 任务完成后删除任务

```
kubectl delete -f job.yaml

查看是否存在
kubectl get jobs
```

![image-20210430111252434](G:\note\image\image-20210430111252434.png)

###### 定时任务

1 yaml文件

```
apiVersion: batch/v1beta1
kind: CronJob  #定时任务
metadata:
  name: hello
spec:
  schedule: "*/1 * * * *"  # 定时
  jobTemplate:
    spec:
      template:
        spec:
          containers:
          - name: hello
            image: busybox
            args:
            - /bin/sh
            - -c
            - date; echo Hello from the Kubernetes cluster
          restartPolicy: OnFailure
```

2 执行yaml文件

3 查看cronjobs

```
kubectl get  cronjobs
```

![image-20210430112216525](G:\note\image\image-20210430112216525.png)

4 查看信息

```
kubectl logs  hello-1619752740-c8ws7
```



#### service

#### ![20-Service](G:\note\image\20-Service.png)

#### secret

###### 概念

1.  Secret 存在意义

Secret 解决了密码、token、密钥等敏感数据的配置问题，而不需要把这些敏感数据暴露
到镜像或者 Pod Spec 中。Secret 可以以 Volume 或者环境变量的方式使用

2. Secret 有三种类型

   • Service Account :用来访问 Kubernetes API,由 Kubernetes 自动创建，并且会自动挂
   载到 Pod 的
   /run/secrets/kubernetes.io/serviceaccount 目录中
   • Opaque : base64 编码格式的 Secret,用来存储密码、密钥等
   • kubernetes.io/dockerconfigjson ：用来存储私有 docker registry 的认证信息

3. 

1 创建secret加密数据

```
apiVersion: v1
kind: Secret
metadata:
  name: mysecret
type: Opaque
data:
  username: YWRtaW4=
  password: MWYyZDFlMmU2N2Rm
```

执行文件

```
kubectl create -f secret.yaml
```

 查看结果

```
kubectl get secret
```

###### 1 以变量挂载

```
apiVersion: v1
kind: Pod
metadata:
  name: mypod
spec:
  containers:
  - name: nginx
    image: nginx
    env:
      - name: SECRET_USERNAME
        valueFrom:
          secretKeyRef:
            name: mysecret
            key: username # SECRET_USERNAME对应secret中的加密username
      - name: SECRET_PASSWORD
        valueFrom:
          secretKeyRef:
            name: mysecret
            key: password

```

3 进入容器

![image-20210430114031108](G:\note\image\image-20210430114031108.png)

###### 2 Volume方式挂载

1 根据yaml创建pod

```
apiVersion: v1
kind: Pod
metadata:
  name: mypod1
spec:
  containers:
  - name: nginx
    image: nginx
    volumeMounts:
    - name: foo
      mountPath: "/etc/foo"
      readOnly: true
  volumes:
  - name: foo
    secret:
      secretName: mysecret

```

2 进入容器

```
kubecl exec -it mypod1 bash
```

3 进入到挂载目录查看数据

![image-20210430115116939](G:\note\image\image-20210430115116939.png)



#### ConfigMap

###### volume挂载

1 创建配置文件

```
redis.host=127.0.0.1
redis.port=6379
redis.password=123456
```

```
把上面文件装维configmap文件
kubectl create configmap redis-config --from-file=redis.properties
```

查看创建的文件

```
kubectl get configmap
```

![image-20210430121825482](G:\note\image\image-20210430121825482.png)

查看文件的详细信息

![image-20210430122013865](G:\note\image\image-20210430122013865.png)

2 挂载到volume

```
apiVersion: v1
kind: Pod
metadata:
  name: mypod2
spec:
  containers:
    - name: busybox
      image: busybox
      command: [ "/bin/sh","-c","cat /etc/config/redis.properties" ]
      volumeMounts:
      - name: config-volume
        mountPath: /etc/config
  volumes:
    - name: config-volume
      configMap:
        name: redis-config
  restartPolicy: Never

```

3 创建好pod之后 查看信息

```
kubectl logs mypod2
```

###### 变量挂载

![image-20210430124441218](G:\note\image\image-20210430124441218.png)

1 创建yaml声明变量

```
apiVersion: v1
kind: ConfigMap
metadata:
  name: myconfig
  namespace: default
data:
  special.level: info
  special.type: hello

```

2 生成configmap配置文件

```
kubectl apply -f myconfig.yaml
```

3 查看结果

```
kubectl get cm
```

![image-20210430123917558](G:\note\image\image-20210430123917558.png)

挂载pod

```
apiVersion: v1
kind: Pod
metadata:
  name: mypod
spec:
  containers:
    - name: busybox
      image: busybox
      command: [ "/bin/sh", "-c", "echo $(LEVEL) $(TYPE)" ]
      env:
        - name: LEVEL  
          valueFrom:
            configMapKeyRef:
              name: myconfig
              key: special.level
        - name: TYPE
          valueFrom:
            configMapKeyRef:
              name: myconfig
              key: special.type
  restartPolicy: Never

```

2 执行文件

```
kubectl apply -f config-var.yaml
```

3 查看结果

```
kubectl logs mypod
```

#### 集群安全

![24-k8s集群安全机制](G:\note\image\24-k8s集群安全机制.png)

###### RBAC鉴权

1 创建命名空间

```
kubectl create ns roledemo
//查看命名空间
kubectl get ns
```

2 在命名空间内创建pod

```
kubectl run nginx --image=nginx -n roledemo

//查看创建的pod
kubectl get pods -n roledemo
```

3 创建角色属于roledemo空间

```
kind: Role
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  namespace: roledemo # 空间
  name: pod-reader
rules:
- apiGroups: [""] # "" indicates the core API group
  resources: ["pods"]
  verbs: ["get", "watch", "list"]
```

```
kubectl apply -f rabc-role.yaml
```

4 创建角色绑定

```
kind: RoleBinding
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: read-pods
  namespace: roledemo #名称空间
subjects:
- kind: User
  name: lucy # Name is case sensitive
  apiGroup: rbac.authorization.k8s.io
roleRef:
  kind: Role #this must be Role or ClusterRole
  name: pod-reader # this must match the name of the Role or ClusterRole you wish to bind to
  apiGroup: rbac.authorization.k8s.io
```

```
kubectl apply -f rabc-rolebinding.yaml
```

查看绑定

```
kubectl get role,rolebinding -n roledemo
```

![image-20210430144043698](G:\note\image\image-20210430144043698.png)

#### Ingress

通过 Service 发现 Pod 进行关联。基于域名访问。
通过 Ingress Controller 实现 Pod 负载均衡
支持 TCP/UDP 4 层负载均衡和 HTTP 7 层负载均衡

###### 1部署 Ingress Controller

```
apiVersion: v1
kind: Namespace
metadata:
  name: ingress-nginx
  labels:
    app.kubernetes.io/name: ingress-nginx
    app.kubernetes.io/part-of: ingress-nginx

---

kind: ConfigMap
apiVersion: v1
metadata:
  name: nginx-configuration
  namespace: ingress-nginx
  labels:
    app.kubernetes.io/name: ingress-nginx
    app.kubernetes.io/part-of: ingress-nginx

---
kind: ConfigMap
apiVersion: v1
metadata:
  name: tcp-services
  namespace: ingress-nginx
  labels:
    app.kubernetes.io/name: ingress-nginx
    app.kubernetes.io/part-of: ingress-nginx

---
kind: ConfigMap
apiVersion: v1
metadata:
  name: udp-services
  namespace: ingress-nginx
  labels:
    app.kubernetes.io/name: ingress-nginx
    app.kubernetes.io/part-of: ingress-nginx

---
apiVersion: v1
kind: ServiceAccount
metadata:
  name: nginx-ingress-serviceaccount
  namespace: ingress-nginx
  labels:
    app.kubernetes.io/name: ingress-nginx
    app.kubernetes.io/part-of: ingress-nginx

---
apiVersion: rbac.authorization.k8s.io/v1beta1
kind: ClusterRole
metadata:
  name: nginx-ingress-clusterrole
  labels:
    app.kubernetes.io/name: ingress-nginx
    app.kubernetes.io/part-of: ingress-nginx
rules:
  - apiGroups:
      - ""
    resources:
      - configmaps
      - endpoints
      - nodes
      - pods
      - secrets
    verbs:
      - list
      - watch
  - apiGroups:
      - ""
    resources:
      - nodes
    verbs:
      - get
  - apiGroups:
      - ""
    resources:
      - services
    verbs:
      - get
      - list
      - watch
  - apiGroups:
      - "extensions"
    resources:
      - ingresses
    verbs:
      - get
      - list
      - watch
  - apiGroups:
      - ""
    resources:
      - events
    verbs:
      - create
      - patch
  - apiGroups:
      - "extensions"
    resources:
      - ingresses/status
    verbs:
      - update

---
apiVersion: rbac.authorization.k8s.io/v1beta1
kind: Role
metadata:
  name: nginx-ingress-role
  namespace: ingress-nginx
  labels:
    app.kubernetes.io/name: ingress-nginx
    app.kubernetes.io/part-of: ingress-nginx
rules:
  - apiGroups:
      - ""
    resources:
      - configmaps
      - pods
      - secrets
      - namespaces
    verbs:
      - get
  - apiGroups:
      - ""
    resources:
      - configmaps
    resourceNames:
      # Defaults to "<election-id>-<ingress-class>"
      # Here: "<ingress-controller-leader>-<nginx>"
      # This has to be adapted if you change either parameter
      # when launching the nginx-ingress-controller.
      - "ingress-controller-leader-nginx"
    verbs:
      - get
      - update
  - apiGroups:
      - ""
    resources:
      - configmaps
    verbs:
      - create
  - apiGroups:
      - ""
    resources:
      - endpoints
    verbs:
      - get

---
apiVersion: rbac.authorization.k8s.io/v1beta1
kind: RoleBinding
metadata:
  name: nginx-ingress-role-nisa-binding
  namespace: ingress-nginx
  labels:
    app.kubernetes.io/name: ingress-nginx
    app.kubernetes.io/part-of: ingress-nginx
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: Role
  name: nginx-ingress-role
subjects:
  - kind: ServiceAccount
    name: nginx-ingress-serviceaccount
    namespace: ingress-nginx

---
apiVersion: rbac.authorization.k8s.io/v1beta1
kind: ClusterRoleBinding
metadata:
  name: nginx-ingress-clusterrole-nisa-binding
  labels:
    app.kubernetes.io/name: ingress-nginx
    app.kubernetes.io/part-of: ingress-nginx
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: nginx-ingress-clusterrole
subjects:
  - kind: ServiceAccount
    name: nginx-ingress-serviceaccount
    namespace: ingress-nginx

---

apiVersion: apps/v1
kind: DaemonSet 
metadata:
  name: nginx-ingress-controller
  namespace: ingress-nginx
  labels:
    app.kubernetes.io/name: ingress-nginx
    app.kubernetes.io/part-of: ingress-nginx
spec:
  selector:
    matchLabels:
      app.kubernetes.io/name: ingress-nginx
      app.kubernetes.io/part-of: ingress-nginx
  template:
    metadata:
      labels:
        app.kubernetes.io/name: ingress-nginx
        app.kubernetes.io/part-of: ingress-nginx
      annotations:
        prometheus.io/port: "10254"
        prometheus.io/scrape: "true"
    spec:
      hostNetwork: true
      serviceAccountName: nginx-ingress-serviceaccount
      containers:
        - name: nginx-ingress-controller
          image: siriuszg/nginx-ingress-controller:0.20.0
          args:
            - /nginx-ingress-controller
            - --configmap=$(POD_NAMESPACE)/nginx-configuration
            - --tcp-services-configmap=$(POD_NAMESPACE)/tcp-services
            - --udp-services-configmap=$(POD_NAMESPACE)/udp-services
            - --publish-service=$(POD_NAMESPACE)/ingress-nginx
            - --annotations-prefix=nginx.ingress.kubernetes.io
          securityContext:
            allowPrivilegeEscalation: true
            capabilities:
              drop:
                - ALL
              add:
                - NET_BIND_SERVICE
            # www-data -> 33
            runAsUser: 33
          env:
            - name: POD_NAME
              valueFrom:
                fieldRef:
                  fieldPath: metadata.name
            - name: POD_NAMESPACE
              valueFrom:
                fieldRef:
                  fieldPath: metadata.namespace
          ports:
            - name: http
              containerPort: 80
            - name: https
              containerPort: 443
          livenessProbe:
            failureThreshold: 3
            httpGet:
              path: /healthz
              port: 10254
              scheme: HTTP
            initialDelaySeconds: 10
            periodSeconds: 10
            successThreshold: 1
            timeoutSeconds: 10
          readinessProbe:
            failureThreshold: 3
            httpGet:
              path: /healthz
              port: 10254
              scheme: HTTP
            periodSeconds: 10
            successThreshold: 1
            timeoutSeconds: 10

---
apiVersion: v1
kind: Service
metadata:
  name: ingress-nginx
  namespace: ingress-nginx
spec:
  #type: NodePort
  ports:
  - name: http
    port: 80
    targetPort: 80
    protocol: TCP
  - name: https
    port: 443
    targetPort: 443
    protocol: TCP
  selector:
    app.kubernetes.io/name: ingress-nginx
    app.kubernetes.io/part-of: ingress-nginx

```

之后 查看pod

```
kubectl get pods --all-namespaces
```

![image-20210501120756404](G:\note\image\image-20210501120756404.png)

###### 2 创建 Ingress 规则

```
apiVersion: extensions/v1beta1
kind: Ingress
metadata:
  name: web
spec:
  rules:
  - host: tomcat6.atguigu.com #绑定的域名
      http:
        paths:
          - backend:
             serviceName: tomcat6 #暴露出去的yaml文件service名字
             servicePort: 80 #pod暴露的端口不是容器端口tomcat容器端口为8080
```



#### helm![28-helm（概述）](G:\note\image\28-helm（概述）.png)

###### 安装helm

1 下载客户端工具 https://github.com/helm/helm/releases 

2 解压 tar zxvf helm-v3.0.0-linux-amd64.tar.gz

3 移动helm文件到 /usr/bin/下

![image-20210430162115395](G:\note\image\image-20210430162115395.png)

配置仓库

```
helm repo add stable http://mirror.azure.cn/kubernetes/charts
helm repo add aliyun https://kubernetes.oss-cn-hangzhou.aliyuncs.com/charts
helm repo update
```

查看仓库

```
helm repo list
```

删除仓库

```
helm repo remove aliyun
```

###### 部署应用

1 搜索应用

```
helm search repo weave 
```

![image-20210430163929390](G:\note\image\image-20210430163929390.png)

2 根据搜索列表安装

```
helm install ui stable/weave-scope   //ui为自定义名字
```

3 查看状态

```
helm list
```

![image-20210430164035961](G:\note\image\image-20210430164035961.png)

4 查看应用

```
 helm status ui
```

![image-20210430164116286](G:\note\image\image-20210430164116286.png)

并没有对外暴露端口修改type

![image-20210430164213603](G:\note\image\image-20210430164213603.png)

修改后

![image-20210430164255475](G:\note\image\image-20210430164255475.png)

使用helm自定义yaml安装应用

![30-helm（自己创建chart）](G:\note\image\30-helm（自己创建chart）.png)1 生成chart

```
helm create mychart
```

2 在mychart/template中加入yaml

3安装执行

```
helm install web3 mychart
```

###### yaml动态传参

![31-helm（chart模板）](G:\note\image\31-helm（chart模板）.png)1 在 创建mychart 中的values.yaml中设置变量

![image-20210430173157583](G:\note\image\image-20210430173157583.png)

2 在 template中的yaml中使用变量

![image-20210430173252320](G:\note\image\image-20210430173252320.png)

#### 数据持久化

###### ![36-持久存储-nfs](G:\note\image\36-持久存储-nfs.png)1 nfs数据存储

   1找一台nfs服务器

1 安装nfs

```
yum install -y nfs-utils
```

2 创建数据目录

```
/data/nfs
```

3 在所有node节点安装nfs

4 在master节点创建pod

```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx-dep1
spec:
  replicas: 1
  selector:
    matchLabels:
      app: nginx
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
      - name: nginx
        image: nginx
        volumeMounts:
        - name: wwwroot
          mountPath: /usr/share/nginx/html  #挂载到pod目录
        ports:
        - containerPort: 80
      volumes:
        - name: wwwroot
          nfs:
            server: 192.168.111.148 #nfs服务器ip
            path: /data/nfs
```

5 创建pod

```
kubectl apply -f nfs-nginx.yaml
```

6 进入pod容器

```
kubectl exec -it pod名字 bash
```

7 在nfs服务器中的/data/nfs目录里新增内容  在新建的pod中依然存在

#### 资源监控

![27-集群监控平台](G:\note\image\27-集群监控平台.png)安装好后使用

```
kubectl get svc  -n kube-system
```

![image-20210430204835272](G:\note\image\image-20210430204835272.png)

#### 集群的基础形式

![image-20210503002050486](G:\note\image\image-20210503002050486.png)

###### mysql主从复制

主mysql开启日志同步功能，从mysql根据主mysql的日志同步数据

1 安装好准备mysql镜像

```
docker pull mysql:5.7
```

2 创建master并挂载文件

```
docker run -p 3307:3306 --name mysql-master \
-v /mydata/mysql/master/log:/var/log/mysql \
-v /mydata/mysql/master/data:/var/lib/mysql \
-v /mydata/mysql/master/conf:/etc/mysql \
-e MYSQL_ROOT_PASSWORD=root \
-d mysql:5.7
参数说明
-p 3307:3306：将容器的 3306 端口映射到主机的 3307 端口
-v /mydata/mysql/master/conf:/etc/mysql：将配置文件夹挂在到主机
-v /mydata/mysql/master/log:/var/log/mysql：将日志文件夹挂载到主机
-v /mydata/mysql/master/data:/var/lib/mysql/：将配置文件夹挂载到主机
-e MYSQL_ROOT_PASSWORD=root：初始化 root 用户的密码
```

 修改master的配置文件

vi /mydata/mysql/master/conf/my.cnf

```
[client]
default-character-set=utf8
[mysql]
default-character-set=utf8
[mysqld]
init_connect='SET collation_connection = utf8_unicode_ci'
init_connect='SET NAMES utf8'
character-set-server=utf8
collation-server=utf8_unicode_ci
skip-character-set-client-handshake
skip-name-resolve
注意：skip-name-resolve 一定要加，不然连接 mysql 会超级慢
```

添加 master 主从复制部分配置

```
server_id=1     #master为1
log-bin=mysql-bin #日志文件
read-only=0
binlog-do-db=gulimall_ums #需要复制的数据库
binlog-do-db=gulimall_pms
binlog-do-db=gulimall_oms
binlog-do-db=gulimall_sms
binlog-do-db=gulimall_wms
binlog-do-db=gulimall_admin
replicate-ignore-db=mysql  #忽略不复制的数据库
replicate-ignore-db=sys
replicate-ignore-db=information_schema
replicate-ignore-db=performance_schema

重启 master
```

3 创建从节点

```
docker run -p 3317:3306 --name mysql-slaver-01 \
-v /mydata/mysql/slaver/log:/var/log/mysql \
-v /mydata/mysql/slaver/data:/var/lib/mysql \
-v /mydata/mysql/slaver/conf:/etc/mysql \
-e MYSQL_ROOT_PASSWORD=root \
-d mysql:5.7
```

```
修改 slave 基本配置
vim /mydata/mysql/slaver/conf/my.cnf
[client]
default-character-set=utf8
[mysql]
default-character-set=utf8
[mysqld]
init_connect='SET collation_connection = utf8_unicode_ci'
init_connect='SET NAMES utf8'
character-set-server=utf8
collation-server=utf8_unicode_ci
skip-character-set-client-handshake
skip-name-resolve
```

```
添加 master 主从复制部分配置
server_id=2
log-bin=mysql-bin
read-only=1
binlog-do-db=gulimall_ums
binlog-do-db=gulimall_pms
binlog-do-db=gulimall_oms
binlog-do-db=gulimall_sms
binlog-do-db=gulimall_wms
binlog-do-db=gulimall_admin
replicate-ignore-db=mysql
replicate-ignore-db=sys
replicate-ignore-db=information_schema
replicate-ignore-db=performance_schema
```

3 为 master 授权用户来他的同步数据

```
1、进入 master 容器
   docker exec -it 容器id /bin/bash

2 进入 mysql 内部 （mysql –uroot -p）

    1）、授权 root 可以远程访问（ 主从无关，为了方便我们远程连接 mysql）
    grant all privileges on *.* to 'root'@'%' identified by 'root' with grant option;
    flush privileges;
    
    2）、添加用来同步的用户
    GRANT REPLICATION SLAVE ON *.* to 'backup'@'%' identified by '123456';
    
3、查看 master 状态
   show master status   
   
```

4 配置 slaver 同步 master 数据

```
1 进入 slaver 容器
    docker exec -it mysql-slaver-01 /bin/bash
    
2、进入 mysql 内部（mysql –uroot -p）

    1）、授权 root 可以远程访问（ 主从无关，为了方便我们远程连接 mysql）
        grant all privileges on *.* to 'root'@'%' identified by 'root' with grant option;
        flush privileges;

    2）、设置主库连接
        SET GLOBAL SQL_SLAVE_SKIP_COUNTER = 1;
        change master to
        master_host='mysql-master.gulimall',master_user='backup',master_password='123456',mas
        ter_log_file='mysql-bin.000003',master_log_pos=0,master_port=3306; 
        
        master_host=master数据的ip
        master_user=设置的name
        master_port=映射的port
   3）、启动从库同步
        start slave;     
   4）、查看从库状态
        show slave status     
```

![image-20210503020344427](G:\note\image\image-20210503020344427.png)

#### 

#### 数据库的分库分表

Sharding-Proxy 下载文件

下载完以后解压需要注意一下，window 环境不要用 7z 等解压工具（因为里面有些文件的文件名过长，解压软件会截断），window 环境下 cmd ,然后执行 ：

```
tar zxvf apache-shardingsphere-4.1.0-sharding-proxy-bin.tar.gz
```

修改配置信息

![image-20210503085009711](G:\note\image\image-20210503085009711.png)

如果要连接mysql还要下载mysql连接驱动 保存到/lib目录下

[Maven Repository: mysql » mysql-connector-java » 5.1.47 (mvnrepository.com)](https://mvnrepository.com/artifact/mysql/mysql-connector-java/5.1.47)



1 数据分片 

```yaml
schemaName: sharding_db  #shardingproxy虚拟的数据库名字 我们连接词虚拟数据库然后转发到实际的数据库

dataSources:
  ds0: #数据源的名字
    url: jdbc:postgresql://localhost:5432/ds0 #实际的数据 mysql/ds0s 数据库
    username: root
    password: 
    connectionTimeoutMilliseconds: 30000
    idleTimeoutMilliseconds: 60000
    maxLifetimeMilliseconds: 1800000
    maxPoolSize: 65
  ds1:
    url: jdbc:postgresql://localhost:5432/ds1
    username: root
    password: 
    connectionTimeoutMilliseconds: 30000
    idleTimeoutMilliseconds: 60000
    maxLifetimeMilliseconds: 1800000
    maxPoolSize: 65

shardingRule:
  tables: #分表规则
    t_order: #t_order表           ds数据源的前缀{0..1} 有两个数据源 
      actualDataNodes: ds${0..1}.t_order${0..1}
      databaseStrategy:    #分库策略
        inline:
          shardingColumn: user_id   #分库的字段
          algorithmExpression: ds${user_id % 2}  # 根据余数
      tableStrategy: 
        inline:
          shardingColumn: order_id
          algorithmExpression: t_order${order_id % 2}
      keyGenerator:
        type: SNOWFLAKE  # 雪花算法生成id
        column: order_id
    t_order_item:
      actualDataNodes: ds${0..1}.t_order_item${0..1}
      databaseStrategy:
        inline:
          shardingColumn: user_id
          algorithmExpression: ds${user_id % 2}
      tableStrategy:
        inline:
          shardingColumn: order_id
          algorithmExpression: t_order_item${order_id % 2}
      keyGenerator:
        type: SNOWFLAKE
        column: order_item_id
  bindingTables:  #绑定表  两张表在同一张数据库
    - t_order,t_order_item
  defaultTableStrategy: #分库策略
    none:
```

server.yaml配置认证信息

![image-20210503091045780](G:\note\image\image-20210503091045780.png)

读写分离

```yaml
schemaName: sharding_db

dataSources:
  master_0_ds: #对每个库读写分离
    url: jdbc:mysql://192.168.111.139:3307/demo_ds_0?serverTimezone=UTC&useSSL=false
    username: root
    password: root
    connectionTimeoutMilliseconds: 30000
    idleTimeoutMilliseconds: 60000
    maxLifetimeMilliseconds: 1800000
    maxPoolSize: 50
  slave_ds_0:
    url: jdbc:mysql://192.168.111.139:3317/demo_ds_0?serverTimezone=UTC&useSSL=false
    username: root
    password: root
    connectionTimeoutMilliseconds: 30000
    idleTimeoutMilliseconds: 60000
    maxLifetimeMilliseconds: 1800000
    maxPoolSize: 50
    
   master_1_ds:
    url: jdbc:mysql://192.168.111.139:3307/demo_ds_1?serverTimezone=UTC&useSSL=false
    username: root
    password: root
    connectionTimeoutMilliseconds: 30000
    idleTimeoutMilliseconds: 60000
    maxLifetimeMilliseconds: 1800000
    maxPoolSize: 50
  slave_ds_1:
    url: jdbc:mysql://192.168.111.139:3317/demo_ds_1?serverTimezone=UTC&useSSL=false
    username: root
    password: root
    connectionTimeoutMilliseconds: 30000
    idleTimeoutMilliseconds: 60000
    maxLifetimeMilliseconds: 1800000
    maxPoolSize: 50  
#  slave_ds_1:
#    url: jdbc:mysql://127.0.0.1:3306/demo_ds_slave_1?serverTimezone=UTC&useSSL=false
#    username: root
#    password:
#    connectionTimeoutMilliseconds: 30000
#    idleTimeoutMilliseconds: 60000
#    maxLifetimeMilliseconds: 1800000
#    maxPoolSize: 50
#
masterSlaveRule: #读写规则
  ms_ds0:  #规则的名字
      masterDataSourceName: master_0_ds #主数据库名字
      slaveDataSourceNames:
        - slave_ds_0
      loadBalanceAlgorithmType: ROUND_ROBIN #轮询规则
   ms_ds1:
      masterDataSourceName: master_1_ds
      slaveDataSourceNames:
        - slave_ds_1
      loadBalanceAlgorithmType: ROUND_ROBIN    

```

手动创建后需要的数据库

运行bin/start文件

使用客户端连接sharding_db数据库

![image-20210503173911450](G:\note\image\image-20210503173911450.png)

#### Redis集群

###### redis-cluster

创建 6 个 redis 节点    3 主 3 从方式，从为了同步备份，主进行 slot 数据分片

```shell
for port in $(seq 7001 7006); \
do \
mkdir -p /mydata/redis/node-${port}/conf
touch /mydata/redis/node-${port}/conf/redis.conf
cat << EOF >/mydata/redis/node-${port}/conf/redis.conf
port ${port}
cluster-enabled yes 
cluster-config-file nodes.conf
cluster-node-timeout 5000
cluster-announce-ip 192.168.111.156
cluster-announce-port ${port}
cluster-announce-bus-port 1${port}
appendonly yes
EOF
docker run -p ${port}:${port} -p 1${port}:1${port} --name redis-${port} \
-v /mydata/redis/node-${port}/data:/data \
-v /mydata/redis/node-${port}/conf/redis.conf:/etc/redis/redis.conf \
-d redis:5.0.7 redis-server /etc/redis/redis.conf; \
done
```

建立集群模式

```shell
docker exec -it redis-7001 bash
redis-cli --cluster create 192.168.111.156:7001 192.168.156.10:7002 192.168.156.10:7003 192.168.156.10:7004 192.168.156.10:7005 192.168.156.10:7006 --cluster-replicas 1
```

![image-20210516220025707](G:\note\image\image-20210516220025707.png)

测试集群效果

随便进入某个 redis 容器

```
docker exec -it redis-7002 /bin/bash

使用 redis-cli 的 cluster 方式进行连接
redis-cli -c -h 192.168.111.156 -p 7006

cluster info； 获取集群信息
cluster nodes；获取集群节点
```

Get/Set 命令测试，将会重定向
节点宕机，slave 会自动提升为 master，master 开启后变为 slave

![image-20210516220438800](G:\note\image\image-20210516220438800.png)

停止 6个redis容器

```
docker stop $(docker ps -a |grep redis-700 | awk '{ print $1}')
```

删除6个redis容器

```
docker rm $(docker ps -a |grep redis-700 | awk '{ print $1}')
```





#### Elasticsearch 集群

###### 1 集群原理

https://www.elastic.co/guide/cn/elasticsearch/guide/current/index.html
https://www.elastic.co/guide/cn/elasticsearch/guide/current/distributed-cluster.html
elasticsearch 是天生支持集群的，他不需要依赖其他的服务发现和注册的组件，如 zookeeper
这些，因为他内置了一个名字叫 ZenDiscovery 的模块，是 elasticsearch 自己实现的一套用
于节点发现和选主等功能的组件，所以 elasticsearch 做起集群来非常简单，不需要太多额外
的配置和安装额外的第三方组件。

1. 一个运行中的 Elasticsearch 实例称为一个节点，而集群是由一个或者多个拥有相同
   cluster.name 配置的节点组成， 它们共同承担数据和负载的压力。当有节点加入集群
   中或者从集群中移除节点时，集群将会重新平均分布所有的数据。
2. 当一个节点被选举成为 主节点时， 它将负责管理集群范围内的所有变更，例如增加、
   删除索引，或者增加、删除节点等。 而主节点并不需要涉及到文档级别的变更和搜索
   等操作，所以当集群只拥有一个主节点的情况下，即使流量的增加它也不会成为瓶颈。
   任何节点都可以成为主节点。我们的示例集群就只有一个节点，所以它同时也成为了主
   节点。
3. 作为用户，我们可以将请求发送到 集群中的任何节点 ，包括主节点。 每个节点都知
   道任意文档所处的位置，并且能够将我们的请求直接转发到存储我们所需文档的节点。
   无论我们将请求发送到哪个节点，它都能负责从各个包含我们所需文档的节点收集回数
   据，并将最终结果返回給客户端。 Elasticsearch 对这一切的管理都是透明的

###### 2 集群健康

Elasticsearch 的集群监控信息中包含了许多的统计数据，其中最为重要的一项就是 集群健
康 ， 它在 status 字段中展示为 green 、 yellow 或者 red 。

```
GET /_cluster/health
```

status 字段指示着当前集群在总体上是否工作正常。它的三种颜色含义如下：
green：所有的主分片和副本分片都正常运行。
yellow：所有的主分片都正常运行，但不是所有的副本分片都正常运行。
red：有主分片没能正常运行

###### 3 分片

1. 一个 分片 是一个底层的 工作单元 ，它仅保存了全部数据中的一部分。我们的文档被
   存储和索引到分片内，但是应用程序是直接与索引而不是与分片进行交互。分片就认为
   是一个数据区

2. 一个分片可以是 主 分片或者 副本 分片。索引内任意一个文档都归属于一个主分片，
   所以主分片的数目决定着索引能够保存的最大数据量。

3. 在索引建立的时候就已经确定了主分片数，但是副本分片数可以随时修改。

4. 让我们在包含一个空节点的集群内创建名为 blogs 的索引。 索引在默认情况下会被分
   配 5 个主分片， 但是为了演示目的，我们将分配 3 个主分片和一份副本（每个主分片
   拥有一个副本分片）：

   ```
   PUT /blogs{
   "settings" : {
   "number_of_shards" : 3,
   "number_of_replicas" : 1
   }}
   ```

###### 4 新增节点

当你在同一台机器上启动了第二个节点时，只要它和第一个节点有同样的 cluster.name 配
置，它就会自动发现集群并加入到其中。 但是在不同机器上启动节点的时候，为了加入到
同一集群，你需要配置一个可连接到的单播主机列表。 详细信息请查看最好使用单播代替
组播

![image-20210504171828730](G:\note\image\image-20210504171828730.png)

此时，cluster-health 现在展示的状态为 green ，这表示所有 6 个分片（包括 3 个主分片和
3 个副本分片）都在正常运行。我们的集群现在不仅仅是正常运行的，并且还处于 始终可
用 的状态

###### 5 水平扩容-启动第三个节点

![image-20210504171910848](G:\note\image\image-20210504171910848.png)

Node 1 和 Node 2 上各有一个分片被迁移到了新的 Node 3 节点，现在每个节点上都拥
有 2 个分片，而不是之前的 3 个。 这表示每个节点的硬件资源（CPU, RAM, I/O）将被更少
的分片所共享，每个分片的性能将会得到提升。

###### 6 应对故障

![image-20210504172012530](G:\note\image\image-20210504172012530.png)

1. 我们关闭的节点是一个主节点。而集群必须拥有一个主节点来保证正常工作，所以发生
   的第一件事情就是选举一个新的主节点： Node 2 。
2. 在我们关闭 Node 1 的同时也失去了主分片 1 和 2 ，并且在缺失主分片的时候索引
   也不能正常工作。 如果此时来检查集群的状况，我们看到的状态将会为 red ：不是所
   有主分片都在正常工作。
3. 幸运的是，在其它节点上存在着这两个主分片的完整副本， 所以新的主节点立即将这
   些分片在 Node 2 和 Node 3 上对应的副本分片提升为主分片， 此时集群的状态将会
   为 yellow 。 这个提升主分片的过程是瞬间发生的，如同按下一个开关一般。
4. 为什么我们集群状态是 yellow 而不是 green 呢？ 虽然我们拥有所有的三个主分片，
   但是同时设置了每个主分片需要对应 2 份副本分片，而此时只存在一份副本分片。 所
   以集群不能为 green 的状态，不过我们不必过于担心：如果我们同样关闭了 Node 2 ，
   我们的程序 依然 可以保持在不丢任何数据的情况下运行，因为 Node 3 为每一个分
   片都保留着一份副本。
5. 如果我们重新启动 Node 1 ，集群可以将缺失的副本分片再次进行分配。如果 Node 1
   依然拥有着之前的分片，它将尝试去重用它们，同时仅从主分片复制发生了修改的数据
   文件。

###### 7 问题与解决

1. 主节点

   主节点负责创建索引、删除索引、分配分片、追踪集群中的节点状态等工作。Elasticsearch
   中的主节点的工作量相对较轻，用户的请求可以发往集群中任何一个节点，由该节点负责分
   发和返回结果，而不需要经过主节点转发。而主节点是由候选主节点通过 ZenDiscovery 机
   制选举出来的，所以要想成为主节点，首先要先成为候选主节点。

2. 候选主节点

   在 elasticsearch 集群初始化或者主节点宕机的情况下，由候选主节点中选举其中一个作为主
   节点。指定候选主节点的配置为：node.master: true。
   当主节点负载压力过大，或者集中环境中的网络问题，导致其他节点与主节点通讯的时候，
   主节点没来的及响应，这样的话，某些节点就认为主节点宕机，重新选择新的主节点，这样
   的话整个集群的工作就有问题了，比如我们集群中有 10 个节点，其中 7 个候选主节点，1
   个候选主节点成为了主节点，这种情况是正常的情况。但是如果现在出现了我们上面所说的
   主节点响应不及时，导致其他某些节点认为主节点宕机而重选主节点，那就有问题了，这剩
   下的 6 个候选主节点可能有 3 个候选主节点去重选主节点，最后集群中就出现了两个主节点
   的情况，这种情况官方成为“**脑裂现象**”；

   集群中不同的节点对于 master 的选择出现了分歧，出现了多个 master 竞争，导致主分片
   和副本的识别也发生了分歧，对一些分歧中的分片标识为了坏片。

3.  数据节点

   数据节点负责数据的存储和相关具体操作，比如 CRUD、搜索、聚合。所以，数据节点对机
   器配置要求比较高，首先需要有足够的磁盘空间来存储数据，其次数据操作对系统 CPU、
   Memory 和 IO 的性能消耗都很大。通常随着集群的扩大，需要增加更多的数据节点来提高
   可用性。指定数据节点的配置：node.data: true。
   elasticsearch 是允许一个节点既做候选主节点也做数据节点的，但是数据节点的负载较重，
   所以需要考虑将二者分离开，设置专用的候选主节点和数据节点，避免因数据节点负载重导
   致主节点不响应。

######  8 脑裂的原因与解决

1. **角色分离**：即 master 节点与 data 节点分离，限制角色；数据节点是需要承担存储
   和搜索的工作的，压力会很大。所以如果该节点同时作为候选主节点和数据节点，
   那么一旦选上它作为主节点了，这时主节点的工作压力将会非常大，出现脑裂现象
   的概率就增加了。

2. **减少误判**：配置主节点的响应时间，在默认情况下，主节点 3 秒没有响应，其他节
   点就认为主节点宕机了，那我们可以把该时间设置的长一点，该配置是：
   discovery.zen.ping_timeout: 5

3. 选举触发discovery.zen.minimum_master_nodes:1（默认是 1），该属性定义的是
   为了形成一个集群，有主节点资格并互相连接的节点的最小数目。
    一 个 有 10 节 点 的 集 群 ， 且 每 个 节 点 都 有 成 为 主 节 点 的 资 格 ，
   discovery.zen.minimum_master_nodes 参数设置为 6。
    正常情况下，10 个节点，互相连接，大于 6，就可以形成一个集群。
    若某个时刻，其中有 3 个节点断开连接。剩下 7 个节点，大于 6，继续运行之
   前的集群。而断开的 3 个节点，小于 6，不能形成一个集群。
    该参数就是为了防止”脑裂”的产生。
    建议设置为(候选主节点数 / 2) + 1,

###### 9 集群结构

以三台物理机为例。在这三台物理机上，搭建了 6 个 ES 的节点，三个 data 节点，三个 master
节点（每台物理机分别起了一个 data 和一个 master），3 个 master 节点，目的是达到（n/2）
+1 等于 2 的要求，这样挂掉一台 master 后（不考虑 data），n 等于 2，满足参数，其他两
个 master 节点都认为 master 挂掉之后开始重新选举，
master 节点上

```
node.master = true
node.data = false
discovery.zen.minimum_master_nodes = 2
```

data 节点上

```
node.master = false
node.data = true
```

![image-20210504173035379](G:\note\image\image-20210504173035379.png)

###### 10 集群搭建

1. 所有之前先运行：sysctl -w vm.max_map_count=262144
   我们只是测试，所以临时修改。永久修改使用下面
   #防止 JVM 报错
   echo vm.max_map_count=262144 >> /etc/sysctl.conf
   sysctl -p

2. Docker 创建容器时默认采用 bridge 网络，自行分配 ip，不允许自己指定。
   在实际部署中，我们需要指定容器 ip，不允许其自行分配 ip，尤其是搭建集群时，固定 ip
   是必须的。
   我们可以创建自己的 bridge 网络 ： mynet，创建容器的时候指定网络为 mynet 并指定 ip
   即可。
   查看网络模式 docker network ls；
   创建一个新的 bridge 网络

   ```
   docker network create --driver bridge --subnet=172.18.12.0/16 --gateway=172.18.1.1
   mynet
   ```

   查看网络信息

   ```
   docker network inspect mynet
   ```

   以后使用--network=mynet --ip 172.18.12.x 指定 ip

3. Master 节点创建

   ```lua
   for port in $(seq 1 3); \
   do \
   mkdir -p /mydata/elasticsearch/master-${port}/config
   mkdir -p /mydata/elasticsearch/master-${port}/data
   chmod -R 777 /mydata/elasticsearch/master-${port}
   cat << EOF >/mydata/elasticsearch/master-${port}/config/elasticsearch.yml
   cluster.name: my-es #集群的名称，同一个集群该值必须设置成相同的
   node.name: es-master-${port} #该节点的名字
   node.master: true #该节点有机会成为 master 节点
   node.data: false #该节点可以存储数据
   network.host: 0.0.0.0
   http.host: 0.0.0.0 #所有 http 均可访问
   http.port: 920${port}
   transport.tcp.port: 930${port}
   #discovery.zen.minimum_master_nodes: 2 #设置这个参数来保证集群中的节点可以知道其它 N 个有 master 资格的节点。官方推荐（N/2）+1
   discovery.zen.ping_timeout: 10s #设置集群中自动发现其他节点时 ping 连接的超时时间
   discovery.seed_hosts: ["172.18.12.21:9301","172.18.12.22:9302","172.18.12.23:9303"] #设置集群中的 Master 节点的初始列表，可以通过这些节点来自动发现其他新加入集群的节点，es7的新增配置
   cluster.initial_master_nodes: ["172.18.12.21"] #新集群初始时的候选主节点，es7 的新增配置
   EOF
   docker run --name elasticsearch-node-${port} \
   -p 920${port}:920${port} -p 930${port}:930${port} \
   --network=mynet --ip 172.18.12.2${port} \
   -e ES_JAVA_OPTS="-Xms300m -Xmx300m" \
   -v /mydata/elasticsearch/master-${port}/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
   -v /mydata/elasticsearch/master-${port}/data:/usr/share/elasticsearch/data \
   -v /mydata/elasticsearch/master-${port}/plugins:/usr/share/elasticsearch/plugins \
   -d elasticsearch:7.4.2
   done
   ```

4. node节点搭建

   ```lua
   for port in $(seq 4 6); \
   do \
   mkdir -p /mydata/elasticsearch/master-${port}/config
   mkdir -p /mydata/elasticsearch/master-${port}/data
   chmod -R 777 /mydata/elasticsearch/master-${port}
   cat << EOF >/mydata/elasticsearch/master-${port}/config/elasticsearch.yml
   cluster.name: my-es #集群的名称，同一个集群该值必须设置成相同的
   node.name: es-master-${port} #该节点的名字
   node.master: false #该节点有机会成为 master 节点
   node.data: true #该节点可以存储数据
   network.host: 0.0.0.0
   http.host: 0.0.0.0 #所有 http 均可访问
   http.port: 920${port}
   transport.tcp.port: 930${port}
   #discovery.zen.minimum_master_nodes: 2 #设置这个参数来保证集群中的节点可以知道其它 N 个有 master 资格的节点。官方推荐（N/2）+1
   discovery.zen.ping_timeout: 10s #设置集群中自动发现其他节点时 ping 连接的超时时间
   discovery.seed_hosts: ["172.18.12.21:9301","172.18.12.22:9302","172.18.12.23:9303"] #设置集群中的 Master 节点的初始列表，可以通过这些节点来自动发现其他新加入集群的节点，es7的新增配置
   cluster.initial_master_nodes: ["172.18.12.21"] #新集群初始时的候选主节点，es7 的新增配置
   EOF
   docker run --name elasticsearch-node-${port} \
   -p 920${port}:920${port} -p 930${port}:930${port} \
   --network=mynet --ip 172.18.12.2${port} \
   -e ES_JAVA_OPTS="-Xms300m -Xmx300m" \
   -v /mydata/elasticsearch/master-${port}/config/elasticsearch.yml:/usr/share/elasticsearch/config/elasticsearch.yml \
   -v /mydata/elasticsearch/master-${port}/data:/usr/share/elasticsearch/data \
   -v /mydata/elasticsearch/master-${port}/plugins:/usr/share/elasticsearch/plugins \
   -d elasticsearch:7.4.2
   done
   ```

   5. 测试集群

      ```
      http://192.168.111.139:9206/_cat/nodes
      /_cat/allocation
      /_cat/shards
      /_cat/shards/{index}
      /_cat/master
      /_cat/nodes
      /_cat/indices
      /_cat/indices/{index}
      /_cat/segments
      /_cat/segments/{index}
      /_cat/count
      /_cat/count/{index}
      /_cat/recovery
      /_cat/recovery/{index}
      /_cat/health
      /_cat/pending_tasks
      /_cat/aliases
      /_cat/aliases/{alias}
      /_cat/thread_pool
      /_cat/plugins
      /_cat/fielddata
      /_cat/fielddata/{fields}
      /_cat/nodeattrs
      /_cat/repositories
      /_cat/snapshots/{repository}
      ```

      ![image-20210504173353642](G:\note\image\image-20210504173353642.png)

#### RabbitMQ 集群

###### 1 集群形式

RabbiMQ 是用 Erlang 开发的，集群非常方便，因为 Erlang 天生就是一门分布式语言，但其本身并不支持负载均衡。

RabbitMQ 集群中节点包括***\*内存节点(RAM)\****、***\*磁盘节点(Disk，消息持久化)\****，集群中至少有一个 Disk 节点。

###### 2 普通模式(默认） 

对于普通模式，集群中各节点有相同的队列结构，但消息只会存在于集群中的一个节点。对于消费者来说，若消息进入 A 节点的 Queue 中，当从 B 节点拉取时，RabbitMQ 会将消息从 A 中取出，并经过 B 发送给消费者。

应用场景：该模式各适合于消息无需持久化的场合，如日志队列。当队列非持久化，且创建该队列的节点宕机，客户端才可以重连集群其他节点，并重新创建队列。若为持久化，   只能等故障节点恢复

###### 3 镜像模式

与普通模式不同之处是消息实体会主动在镜像节点间同步，而不是在取数据时临时拉取，高可用；该模式下，mirror queue 有一套选举算法，即 1 个 master、n 个 slaver，生产者、消费者的请求都会转至 master。

应用场景：可靠性要求较高场合，如下单、库存队列。

缺点：若镜像队列过多，且消息体量大，集群内部网络带宽将会被此种同步通讯所消

耗。

（1） 镜像集群也是基于普通集群，即只有先搭建普通集群，然后才能设置镜像队列。

若消费过程中，master 挂掉，则选举新 master，若未来得及确认，则可能会重复消费。

###### 4 搭建集群

1. 准备文件夹

```
mkdir /mydata/rabbitmq 
cd rabbitmq/
mkdir rabbitmq01 rabbitmq02 rabbitmq03
```

3. 生成容器

   ```
   docker run -d --hostname rabbitmq01 --name rabbitmq01 \
   -v /mydata/rabbitmq/rabbitmq01:/var/lib/rabbitmq \
   -p 15673:15672 -p 5673:5672 \
   -e RABBITMQ_ERLANG_COOKIE='atguigu' rabbitmq:management
   
   docker run -d --hostname rabbitmq02 --name rabbitmq02 \
   -v /mydata/rabbitmq/rabbitmq02:/var/lib/rabbitmq \
   -p 15674:15672 -p 5674:5672 \
   -e RABBITMQ_ERLANG_COOKIE='atguigu' \
   --link rabbitmq01:rabbitmq01 \
   rabbitmq:management
   
   docker run -d --hostname rabbitmq03 --name rabbitmq03 \
   -v /mydata/rabbitmq/rabbitmq03:/var/lib/rabbitmq \
   -p 15675:15672 -p 5675:5672 \
   -e RABBITMQ_ERLANG_COOKIE='atguigu' \
   --link rabbitmq01:rabbitmq01 \
   --link rabbitmq02:rabbitmq02 \
   rabbitmq:management
   
   ```

4. 节点加入

   ```
   进入第一个节点
   docker exec -it rabbitmq01 /bin/bash
   rabbitmqctl stop_app rabbitmqctl reset rabbitmqctl start_app
   Exit
   进入第二个节点
   docker exec -it rabbitmq02 /bin/bash 
   rabbitmqctl stop_app
   rabbitmqctl reset
   rabbitmqctl join_cluster --ram rabbit@rabbitmq01 
   rabbitmqctl start_app
   exit
   
   
   进入第三个节点
   docker exec -it rabbitmq03 bash
   rabbitmqctl stop_app
   rabbitmqctl reset
   rabbitmqctl join_cluster --ram rabbit@rabbitmq01 
   rabbitmqctl start_app
   exit
   ```

   4. 实现镜像集群

```
docker exec -it rabbitmq01 bash

rabbitmqctl set_policy -p / ha "^" '{"ha-mode":"all","ha-sync-mode":"automatic"}'

可以使用 rabbitmqctl list_policies -p / 查看 vhost/下面的所有 policy

在 cluster 中任意节点启用策略，策略会自动同步到集群节点rabbitmqctl set_policy-p/ha-all"^"’{“ha-mode”:“all”}’
策略模式 all 即复制到所有节点，包含新增节点，策略正则表达式为 “^” 表示所有匹配所
```

#### kubesphere

###### 1 准备环境

 前提安装好docker kubectl

确认 master 节点是否有 Taint，如下看到 master 节点有 Taint。

```
kubectl describe node master | grep Taint
Taints:             node-role.kubernetes.io/master:NoSchedule
```

去掉master的Taint

```
kubectl taint nodes master node-role.kubernetes.io/master:NoSchedule-
```



###### 2 安装openobs

```
kubectl apply -f openebs.yaml

查看创建的 StorageClass
kubectl get sc

将 openebs-hostpath设置为 默认的 StorageClass

kubectl patch storageclass openebs-hostpath -p '{"metadata": {"annotations":{"storageclass.kubernetes.io/is-default-class":"true"}}}'
```

```
# This manifest deploys the OpenEBS control plane components, with associated CRs & RBAC rules
# NOTE: On GKE, deploy the openebs-operator.yaml in admin context

# Create the OpenEBS namespace
apiVersion: v1
kind: Namespace
metadata:
  name: openebs
---
# Create Maya Service Account
apiVersion: v1
kind: ServiceAccount
metadata:
  name: openebs-maya-operator
  namespace: openebs
---
# Define Role that allows operations on K8s pods/deployments
kind: ClusterRole
apiVersion: rbac.authorization.k8s.io/v1beta1
metadata:
  name: openebs-maya-operator
rules:
- apiGroups: ["*"]
  resources: ["nodes", "nodes/proxy"]
  verbs: ["*"]
- apiGroups: ["*"]
  resources: ["namespaces", "services", "pods", "pods/exec", "deployments", "deployments/finalizers", "replicationcontrollers", "replicasets", "events", "endpoints", "configmaps", "secrets", "jobs", "cronjobs"]
  verbs: ["*"]
- apiGroups: ["*"]
  resources: ["statefulsets", "daemonsets"]
  verbs: ["*"]
- apiGroups: ["*"]
  resources: ["resourcequotas", "limitranges"]
  verbs: ["list", "watch"]
- apiGroups: ["*"]
  resources: ["ingresses", "horizontalpodautoscalers", "verticalpodautoscalers", "poddisruptionbudgets", "certificatesigningrequests"]
  verbs: ["list", "watch"]
- apiGroups: ["*"]
  resources: ["storageclasses", "persistentvolumeclaims", "persistentvolumes"]
  verbs: ["*"]
- apiGroups: ["volumesnapshot.external-storage.k8s.io"]
  resources: ["volumesnapshots", "volumesnapshotdatas"]
  verbs: ["get", "list", "watch", "create", "update", "patch", "delete"]
- apiGroups: ["apiextensions.k8s.io"]
  resources: ["customresourcedefinitions"]
  verbs: [ "get", "list", "create", "update", "delete", "patch"]
- apiGroups: ["*"]
  resources: [ "disks", "blockdevices", "blockdeviceclaims"]
  verbs: ["*" ]
- apiGroups: ["*"]
  resources: [ "cstorpoolclusters", "storagepoolclaims", "storagepoolclaims/finalizers", "cstorpoolclusters/finalizers", "storagepools"]
  verbs: ["*" ]
- apiGroups: ["*"]
  resources: [ "castemplates", "runtasks"]
  verbs: ["*" ]
- apiGroups: ["*"]
  resources: [ "cstorpools", "cstorpools/finalizers", "cstorvolumereplicas", "cstorvolumes", "cstorvolumeclaims"]
  verbs: ["*" ]
- apiGroups: ["*"]
  resources: [ "cstorpoolinstances", "cstorpoolinstances/finalizers"]
  verbs: ["*" ]
- apiGroups: ["*"]
  resources: [ "cstorbackups", "cstorrestores", "cstorcompletedbackups"]
  verbs: ["*" ]
- apiGroups: ["coordination.k8s.io"]
  resources: ["leases"]
  verbs: ["get", "watch", "list", "delete", "update", "create"]
- apiGroups: ["admissionregistration.k8s.io"]
  resources: ["validatingwebhookconfigurations", "mutatingwebhookconfigurations"]
  verbs: ["get", "create", "list", "delete", "update", "patch"]
- nonResourceURLs: ["/metrics"]
  verbs: ["get"]
- apiGroups: ["*"]
  resources: [ "upgradetasks"]
  verbs: ["*" ]
---
# Bind the Service Account with the Role Privileges.
# TODO: Check if default account also needs to be there
kind: ClusterRoleBinding
apiVersion: rbac.authorization.k8s.io/v1beta1
metadata:
  name: openebs-maya-operator
subjects:
- kind: ServiceAccount
  name: openebs-maya-operator
  namespace: openebs
roleRef:
  kind: ClusterRole
  name: openebs-maya-operator
  apiGroup: rbac.authorization.k8s.io
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: maya-apiserver
  namespace: openebs
  labels:
    name: maya-apiserver
    openebs.io/component-name: maya-apiserver
    openebs.io/version: 1.5.0
spec:
  selector:
    matchLabels:
      name: maya-apiserver
      openebs.io/component-name: maya-apiserver
  replicas: 1
  strategy:
    type: Recreate
    rollingUpdate: null
  template:
    metadata:
      labels:
        name: maya-apiserver
        openebs.io/component-name: maya-apiserver
        openebs.io/version: 1.5.0
    spec:
      serviceAccountName: openebs-maya-operator
      containers:
      - name: maya-apiserver
        imagePullPolicy: IfNotPresent
        image: openebs/m-apiserver:1.5.0
        ports:
        - containerPort: 5656
        env:
        # OPENEBS_IO_KUBE_CONFIG enables maya api service to connect to K8s
        # based on this config. This is ignored if empty.
        # This is supported for maya api server version 0.5.2 onwards
        #- name: OPENEBS_IO_KUBE_CONFIG
        #  value: "/home/ubuntu/.kube/config"
        # OPENEBS_IO_K8S_MASTER enables maya api service to connect to K8s
        # based on this address. This is ignored if empty.
        # This is supported for maya api server version 0.5.2 onwards
        #- name: OPENEBS_IO_K8S_MASTER
        #  value: "http://172.28.128.3:8080"
        # OPENEBS_NAMESPACE provides the namespace of this deployment as an
        # environment variable
        - name: OPENEBS_NAMESPACE
          valueFrom:
            fieldRef:
              fieldPath: metadata.namespace
        # OPENEBS_SERVICE_ACCOUNT provides the service account of this pod as
        # environment variable
        - name: OPENEBS_SERVICE_ACCOUNT
          valueFrom:
            fieldRef:
              fieldPath: spec.serviceAccountName
        # OPENEBS_MAYA_POD_NAME provides the name of this pod as
        # environment variable
        - name: OPENEBS_MAYA_POD_NAME
          valueFrom:
            fieldRef:
              fieldPath: metadata.name
        # If OPENEBS_IO_CREATE_DEFAULT_STORAGE_CONFIG is false then OpenEBS default
        # storageclass and storagepool will not be created.
        - name: OPENEBS_IO_CREATE_DEFAULT_STORAGE_CONFIG
          value: "true"
        # OPENEBS_IO_INSTALL_DEFAULT_CSTOR_SPARSE_POOL decides whether default cstor sparse pool should be
        # configured as a part of openebs installation.
        # If "true" a default cstor sparse pool will be configured, if "false" it will not be configured.
        # This value takes effect only if OPENEBS_IO_CREATE_DEFAULT_STORAGE_CONFIG
        # is set to true
        - name: OPENEBS_IO_INSTALL_DEFAULT_CSTOR_SPARSE_POOL
          value: "false"
        # OPENEBS_IO_CSTOR_TARGET_DIR can be used to specify the hostpath
        # to be used for saving the shared content between the side cars
        # of cstor volume pod.
        # The default path used is /var/openebs/sparse
        #- name: OPENEBS_IO_CSTOR_TARGET_DIR
        #  value: "/var/openebs/sparse"
        # OPENEBS_IO_CSTOR_POOL_SPARSE_DIR can be used to specify the hostpath
        # to be used for saving the shared content between the side cars
        # of cstor pool pod. This ENV is also used to indicate the location
        # of the sparse devices.
        # The default path used is /var/openebs/sparse
        #- name: OPENEBS_IO_CSTOR_POOL_SPARSE_DIR
        #  value: "/var/openebs/sparse"
        # OPENEBS_IO_JIVA_POOL_DIR can be used to specify the hostpath
        # to be used for default Jiva StoragePool loaded by OpenEBS
        # The default path used is /var/openebs
        # This value takes effect only if OPENEBS_IO_CREATE_DEFAULT_STORAGE_CONFIG
        # is set to true
        #- name: OPENEBS_IO_JIVA_POOL_DIR
        #  value: "/var/openebs"
        # OPENEBS_IO_LOCALPV_HOSTPATH_DIR can be used to specify the hostpath
        # to be used for default openebs-hostpath storageclass loaded by OpenEBS
        # The default path used is /var/openebs/local
        # This value takes effect only if OPENEBS_IO_CREATE_DEFAULT_STORAGE_CONFIG
        # is set to true
        #- name: OPENEBS_IO_LOCALPV_HOSTPATH_DIR
        #  value: "/var/openebs/local"
        - name: OPENEBS_IO_JIVA_CONTROLLER_IMAGE
          value: "openebs/jiva:1.5.0"
        - name: OPENEBS_IO_JIVA_REPLICA_IMAGE
          value: "openebs/jiva:1.5.0"
        - name: OPENEBS_IO_JIVA_REPLICA_COUNT
          value: "3"
        - name: OPENEBS_IO_CSTOR_TARGET_IMAGE
          value: "openebs/cstor-istgt:1.5.0"
        - name: OPENEBS_IO_CSTOR_POOL_IMAGE
          value: "openebs/cstor-pool:1.5.0"
        - name: OPENEBS_IO_CSTOR_POOL_MGMT_IMAGE
          value: "openebs/cstor-pool-mgmt:1.5.0"
        - name: OPENEBS_IO_CSTOR_VOLUME_MGMT_IMAGE
          value: "openebs/cstor-volume-mgmt:1.5.0"
        - name: OPENEBS_IO_VOLUME_MONITOR_IMAGE
          value: "openebs/m-exporter:1.5.0"
        - name: OPENEBS_IO_CSTOR_POOL_EXPORTER_IMAGE
          value: "openebs/m-exporter:1.5.0"
        - name: OPENEBS_IO_HELPER_IMAGE
          value: "openebs/linux-utils:1.5.0"
        # OPENEBS_IO_ENABLE_ANALYTICS if set to true sends anonymous usage
        # events to Google Analytics
        - name: OPENEBS_IO_ENABLE_ANALYTICS
          value: "true"
        - name: OPENEBS_IO_INSTALLER_TYPE
          value: "openebs-operator"
        # OPENEBS_IO_ANALYTICS_PING_INTERVAL can be used to specify the duration (in hours)
        # for periodic ping events sent to Google Analytics.
        # Default is 24h.
        # Minimum is 1h. You can convert this to weekly by setting 168h
        #- name: OPENEBS_IO_ANALYTICS_PING_INTERVAL
        #  value: "24h"
        livenessProbe:
          exec:
            command:
            - /usr/local/bin/mayactl
            - version
          initialDelaySeconds: 30
          periodSeconds: 60
        readinessProbe:
          exec:
            command:
            - /usr/local/bin/mayactl
            - version
          initialDelaySeconds: 30
          periodSeconds: 60
---
apiVersion: v1
kind: Service
metadata:
  name: maya-apiserver-service
  namespace: openebs
  labels:
    openebs.io/component-name: maya-apiserver-svc
spec:
  ports:
  - name: api
    port: 5656
    protocol: TCP
    targetPort: 5656
  selector:
    name: maya-apiserver
  sessionAffinity: None
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: openebs-provisioner
  namespace: openebs
  labels:
    name: openebs-provisioner
    openebs.io/component-name: openebs-provisioner
    openebs.io/version: 1.5.0
spec:
  selector:
    matchLabels:
      name: openebs-provisioner
      openebs.io/component-name: openebs-provisioner
  replicas: 1
  strategy:
    type: Recreate
    rollingUpdate: null
  template:
    metadata:
      labels:
        name: openebs-provisioner
        openebs.io/component-name: openebs-provisioner
        openebs.io/version: 1.5.0
    spec:
      serviceAccountName: openebs-maya-operator
      containers:
      - name: openebs-provisioner
        imagePullPolicy: IfNotPresent
        image: openebs/openebs-k8s-provisioner:1.5.0
        env:
        # OPENEBS_IO_K8S_MASTER enables openebs provisioner to connect to K8s
        # based on this address. This is ignored if empty.
        # This is supported for openebs provisioner version 0.5.2 onwards
        #- name: OPENEBS_IO_K8S_MASTER
        #  value: "http://10.128.0.12:8080"
        # OPENEBS_IO_KUBE_CONFIG enables openebs provisioner to connect to K8s
        # based on this config. This is ignored if empty.
        # This is supported for openebs provisioner version 0.5.2 onwards
        #- name: OPENEBS_IO_KUBE_CONFIG
        #  value: "/home/ubuntu/.kube/config"
        - name: NODE_NAME
          valueFrom:
            fieldRef:
              fieldPath: spec.nodeName
        - name: OPENEBS_NAMESPACE
          valueFrom:
            fieldRef:
              fieldPath: metadata.namespace
        # OPENEBS_MAYA_SERVICE_NAME provides the maya-apiserver K8s service name,
        # that provisioner should forward the volume create/delete requests.
        # If not present, "maya-apiserver-service" will be used for lookup.
        # This is supported for openebs provisioner version 0.5.3-RC1 onwards
        #- name: OPENEBS_MAYA_SERVICE_NAME
        #  value: "maya-apiserver-apiservice"
        livenessProbe:
          exec:
            command:
            - pgrep
            - ".*openebs"
          initialDelaySeconds: 30
          periodSeconds: 60
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: openebs-snapshot-operator
  namespace: openebs
  labels:
    name: openebs-snapshot-operator
    openebs.io/component-name: openebs-snapshot-operator
    openebs.io/version: 1.5.0
spec:
  selector:
    matchLabels:
      name: openebs-snapshot-operator
      openebs.io/component-name: openebs-snapshot-operator
  replicas: 1
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        name: openebs-snapshot-operator
        openebs.io/component-name: openebs-snapshot-operator
        openebs.io/version: 1.5.0
    spec:
      serviceAccountName: openebs-maya-operator
      containers:
        - name: snapshot-controller
          image: openebs/snapshot-controller:1.5.0
          imagePullPolicy: IfNotPresent
          env:
          - name: OPENEBS_NAMESPACE
            valueFrom:
              fieldRef:
                fieldPath: metadata.namespace
          livenessProbe:
            exec:
              command:
              - pgrep
              - ".*controller"
            initialDelaySeconds: 30
            periodSeconds: 60
        # OPENEBS_MAYA_SERVICE_NAME provides the maya-apiserver K8s service name,
        # that snapshot controller should forward the snapshot create/delete requests.
        # If not present, "maya-apiserver-service" will be used for lookup.
        # This is supported for openebs provisioner version 0.5.3-RC1 onwards
        #- name: OPENEBS_MAYA_SERVICE_NAME
        #  value: "maya-apiserver-apiservice"
        - name: snapshot-provisioner
          image: openebs/snapshot-provisioner:1.5.0
          imagePullPolicy: IfNotPresent
          env:
          - name: OPENEBS_NAMESPACE
            valueFrom:
              fieldRef:
                fieldPath: metadata.namespace
        # OPENEBS_MAYA_SERVICE_NAME provides the maya-apiserver K8s service name,
        # that snapshot provisioner  should forward the clone create/delete requests.
        # If not present, "maya-apiserver-service" will be used for lookup.
        # This is supported for openebs provisioner version 0.5.3-RC1 onwards
        #- name: OPENEBS_MAYA_SERVICE_NAME
        #  value: "maya-apiserver-apiservice"
          livenessProbe:
            exec:
              command:
              - pgrep
              - ".*provisioner"
            initialDelaySeconds: 30
            periodSeconds: 60
---
# This is the node-disk-manager related config.
# It can be used to customize the disks probes and filters
apiVersion: v1
kind: ConfigMap
metadata:
  name: openebs-ndm-config
  namespace: openebs
  labels:
    openebs.io/component-name: ndm-config
data:
  # udev-probe is default or primary probe which should be enabled to run ndm
  # filterconfigs contails configs of filters - in their form fo include
  # and exclude comma separated strings
  node-disk-manager.config: |
    probeconfigs:
      - key: udev-probe
        name: udev probe
        state: true
      - key: seachest-probe
        name: seachest probe
        state: false
      - key: smart-probe
        name: smart probe
        state: true
    filterconfigs:
      - key: os-disk-exclude-filter
        name: os disk exclude filter
        state: true
        exclude: "/,/etc/hosts,/boot"
      - key: vendor-filter
        name: vendor filter
        state: true
        include: ""
        exclude: "CLOUDBYT,OpenEBS"
      - key: path-filter
        name: path filter
        state: true
        include: ""
        exclude: "loop,/dev/fd0,/dev/sr0,/dev/ram,/dev/dm-,/dev/md"
---
apiVersion: apps/v1
kind: DaemonSet
metadata:
  name: openebs-ndm
  namespace: openebs
  labels:
    name: openebs-ndm
    openebs.io/component-name: ndm
    openebs.io/version: 1.5.0
spec:
  selector:
    matchLabels:
      name: openebs-ndm
      openebs.io/component-name: ndm
  updateStrategy:
    type: RollingUpdate
  template:
    metadata:
      labels:
        name: openebs-ndm
        openebs.io/component-name: ndm
        openebs.io/version: 1.5.0
    spec:
      # By default the node-disk-manager will be run on all kubernetes nodes
      # If you would like to limit this to only some nodes, say the nodes
      # that have storage attached, you could label those node and use
      # nodeSelector.
      #
      # e.g. label the storage nodes with - "openebs.io/nodegroup"="storage-node"
      # kubectl label node <node-name> "openebs.io/nodegroup"="storage-node"
      #nodeSelector:
      #  "openebs.io/nodegroup": "storage-node"
      serviceAccountName: openebs-maya-operator
      hostNetwork: true
      containers:
      - name: node-disk-manager
        image: openebs/node-disk-manager-amd64:v0.4.5
        imagePullPolicy: Always
        securityContext:
          privileged: true
        volumeMounts:
        - name: config
          mountPath: /host/node-disk-manager.config
          subPath: node-disk-manager.config
          readOnly: true
        - name: udev
          mountPath: /run/udev
        - name: procmount
          mountPath: /host/proc
          readOnly: true
        - name: sparsepath
          mountPath: /var/openebs/sparse
        env:
        # namespace in which NDM is installed will be passed to NDM Daemonset
        # as environment variable
        - name: NAMESPACE
          valueFrom:
            fieldRef:
              fieldPath: metadata.namespace
        # pass hostname as env variable using downward API to the NDM container
        - name: NODE_NAME
          valueFrom:
            fieldRef:
              fieldPath: spec.nodeName
        # specify the directory where the sparse files need to be created.
        # if not specified, then sparse files will not be created.
        - name: SPARSE_FILE_DIR
          value: "/var/openebs/sparse"
        # Size(bytes) of the sparse file to be created.
        - name: SPARSE_FILE_SIZE
          value: "10737418240"
        # Specify the number of sparse files to be created
        - name: SPARSE_FILE_COUNT
          value: "0"
        livenessProbe:
          exec:
            command:
            - pgrep
            - ".*ndm"
          initialDelaySeconds: 30
          periodSeconds: 60
      volumes:
      - name: config
        configMap:
          name: openebs-ndm-config
      - name: udev
        hostPath:
          path: /run/udev
          type: Directory
      # mount /proc (to access mount file of process 1 of host) inside container
      # to read mount-point of disks and partitions
      - name: procmount
        hostPath:
          path: /proc
          type: Directory
      - name: sparsepath
        hostPath:
          path: /var/openebs/sparse
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: openebs-ndm-operator
  namespace: openebs
  labels:
    name: openebs-ndm-operator
    openebs.io/component-name: ndm-operator
    openebs.io/version: 1.5.0
spec:
  selector:
    matchLabels:
      name: openebs-ndm-operator
      openebs.io/component-name: ndm-operator
  replicas: 1
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        name: openebs-ndm-operator
        openebs.io/component-name: ndm-operator
        openebs.io/version: 1.5.0
    spec:
      serviceAccountName: openebs-maya-operator
      containers:
        - name: node-disk-operator
          image: openebs/node-disk-operator-amd64:v0.4.5
          imagePullPolicy: Always
          readinessProbe:
            exec:
              command:
                - stat
                - /tmp/operator-sdk-ready
            initialDelaySeconds: 4
            periodSeconds: 10
            failureThreshold: 1
          env:
            - name: WATCH_NAMESPACE
              valueFrom:
                fieldRef:
                  fieldPath: metadata.namespace
            - name: POD_NAME
              valueFrom:
                fieldRef:
                  fieldPath: metadata.name
            # the service account of the ndm-operator pod
            - name: SERVICE_ACCOUNT
              valueFrom:
                fieldRef:
                  fieldPath: spec.serviceAccountName
            - name: OPERATOR_NAME
              value: "node-disk-operator"
            - name: CLEANUP_JOB_IMAGE
              value: "openebs/linux-utils:1.5.0"
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: openebs-admission-server
  namespace: openebs
  labels:
    app: admission-webhook
    openebs.io/component-name: admission-webhook
    openebs.io/version: 1.5.0
spec:
  replicas: 1
  strategy:
    type: Recreate
    rollingUpdate: null
  selector:
    matchLabels:
      app: admission-webhook
  template:
    metadata:
      labels:
        app: admission-webhook
        openebs.io/component-name: admission-webhook
        openebs.io/version: 1.5.0
    spec:
      serviceAccountName: openebs-maya-operator
      containers:
        - name: admission-webhook
          image: openebs/admission-server:1.5.0
          imagePullPolicy: IfNotPresent
          args:
            - -alsologtostderr
            - -v=2
            - 2>&1
          env:
            - name: OPENEBS_NAMESPACE
              valueFrom:
                fieldRef:
                  fieldPath: metadata.namespace
            - name: ADMISSION_WEBHOOK_NAME
              value: "openebs-admission-server"
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: openebs-localpv-provisioner
  namespace: openebs
  labels:
    name: openebs-localpv-provisioner
    openebs.io/component-name: openebs-localpv-provisioner
    openebs.io/version: 1.5.0
spec:
  selector:
    matchLabels:
      name: openebs-localpv-provisioner
      openebs.io/component-name: openebs-localpv-provisioner
  replicas: 1
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        name: openebs-localpv-provisioner
        openebs.io/component-name: openebs-localpv-provisioner
        openebs.io/version: 1.5.0
    spec:
      serviceAccountName: openebs-maya-operator
      containers:
      - name: openebs-provisioner-hostpath
        imagePullPolicy: Always
        image: openebs/provisioner-localpv:1.5.0
        env:
        # OPENEBS_IO_K8S_MASTER enables openebs provisioner to connect to K8s
        # based on this address. This is ignored if empty.
        # This is supported for openebs provisioner version 0.5.2 onwards
        #- name: OPENEBS_IO_K8S_MASTER
        #  value: "http://10.128.0.12:8080"
        # OPENEBS_IO_KUBE_CONFIG enables openebs provisioner to connect to K8s
        # based on this config. This is ignored if empty.
        # This is supported for openebs provisioner version 0.5.2 onwards
        #- name: OPENEBS_IO_KUBE_CONFIG
        #  value: "/home/ubuntu/.kube/config"
        - name: NODE_NAME
          valueFrom:
            fieldRef:
              fieldPath: spec.nodeName
        - name: OPENEBS_NAMESPACE
          valueFrom:
            fieldRef:
              fieldPath: metadata.namespace
        # OPENEBS_SERVICE_ACCOUNT provides the service account of this pod as
        # environment variable
        - name: OPENEBS_SERVICE_ACCOUNT
          valueFrom:
            fieldRef:
              fieldPath: spec.serviceAccountName
        - name: OPENEBS_IO_ENABLE_ANALYTICS
          value: "true"
        - name: OPENEBS_IO_INSTALLER_TYPE
          value: "openebs-operator"
        - name: OPENEBS_IO_HELPER_IMAGE
          value: "openebs/linux-utils:1.5.0"
        livenessProbe:
          exec:
            command:
            - pgrep
            - ".*localpv"
          initialDelaySeconds: 30
          periodSeconds: 60
---
```



###### 3 安装kubesphere

```
kubectl apply -f https://github.com/kubesphere/ks-installer/releases/download/v3.0.0/kubesphere-installer.yaml
   
kubectl apply -f https://github.com/kubesphere/ks-installer/releases/download/v3.0.0/cluster-configuration.yaml

```

```
---
apiVersion: installer.kubesphere.io/v1alpha1
kind: ClusterConfiguration
metadata:
  name: ks-installer
  namespace: kubesphere-system
  labels:
    version: v3.0.0
spec:
  persistence:
    storageClass: ""        # If there is not a default StorageClass in your cluster, you need to specify an existing StorageClass here.
  authentication:
    jwtSecret: ""           # Keep the jwtSecret consistent with the host cluster. Retrive the jwtSecret by executing "kubectl -n kubesphere-system get cm kubesphere-config -o yaml | grep -v "apiVersion" | grep jwtSecret" on the host cluster.
  etcd:
    monitoring: false       # Whether to enable etcd monitoring dashboard installation. You have to create a secret for etcd before you enable it.
    endpointIps: localhost  # etcd cluster EndpointIps, it can be a bunch of IPs here.
    port: 2379              # etcd port
    tlsEnable: true
  common:
    mysqlVolumeSize: 20Gi # MySQL PVC size.
    minioVolumeSize: 20Gi # Minio PVC size.
    etcdVolumeSize: 20Gi  # etcd PVC size.
    openldapVolumeSize: 2Gi   # openldap PVC size.
    redisVolumSize: 2Gi # Redis PVC size.
    es:   # Storage backend for logging, events and auditing.
      # elasticsearchMasterReplicas: 1   # total number of master nodes, it's not allowed to use even number
      # elasticsearchDataReplicas: 1     # total number of data nodes.
      elasticsearchMasterVolumeSize: 4Gi   # Volume size of Elasticsearch master nodes.
      elasticsearchDataVolumeSize: 20Gi    # Volume size of Elasticsearch data nodes.
      logMaxAge: 7                     # Log retention time in built-in Elasticsearch, it is 7 days by default.
      elkPrefix: logstash              # The string making up index names. The index name will be formatted as ks-<elk_prefix>-log.
  console:
    enableMultiLogin: true  # enable/disable multiple sing on, it allows an account can be used by different users at the same time.
    port: 30880
  alerting:                # (CPU: 0.3 Core, Memory: 300 MiB) Whether to install KubeSphere alerting system. It enables Users to customize alerting policies to send messages to receivers in time with different time intervals and alerting levels to choose from.
    enabled: true
  auditing:                # Whether to install KubeSphere audit log system. It provides a security-relevant chronological set of records，recording the sequence of activities happened in platform, initiated by different tenants.
    enabled: true
  devops:                  # (CPU: 0.47 Core, Memory: 8.6 G) Whether to install KubeSphere DevOps System. It provides out-of-box CI/CD system based on Jenkins, and automated workflow tools including Source-to-Image & Binary-to-Image.
    enabled: true
    jenkinsMemoryLim: 2Gi      # Jenkins memory limit.
    jenkinsMemoryReq: 1500Mi   # Jenkins memory request.
    jenkinsVolumeSize: 8Gi     # Jenkins volume size.
    jenkinsJavaOpts_Xms: 512m  # The following three fields are JVM parameters.
    jenkinsJavaOpts_Xmx: 512m
    jenkinsJavaOpts_MaxRAM: 2g
    sonarqube_enabled: true
  events:                  # Whether to install KubeSphere events system. It provides a graphical web console for Kubernetes Events exporting, filtering and alerting in multi-tenant Kubernetes clusters.
    enabled: false
    ruler:
      enabled: true
      replicas: 2
  logging:                 # (CPU: 57 m, Memory: 2.76 G) Whether to install KubeSphere logging system. Flexible logging functions are provided for log query, collection and management in a unified console. Additional log collectors can be added, such as Elasticsearch, Kafka and Fluentd.
    enabled: true
    logsidecarReplicas: 2
  metrics_server:                    # (CPU: 56 m, Memory: 44.35 MiB) Whether to install metrics-server. IT enables HPA (Horizontal Pod Autoscaler).
    enabled: true
  monitoring:
    # prometheusReplicas: 1            # Prometheus replicas are responsible for monitoring different segments of data source and provide high availability as well.
    prometheusMemoryRequest: 400Mi   # Prometheus request memory.
    prometheusVolumeSize: 20Gi       # Prometheus PVC size.
    # alertmanagerReplicas: 1          # AlertManager Replicas.
  multicluster:
    clusterRole: none  # host | member | none  # You can install a solo cluster, or specify it as the role of host or member cluster.
  networkpolicy:       # Network policies allow network isolation within the same cluster, which means firewalls can be set up between certain instances (Pods).
    # Make sure that the CNI network plugin used by the cluster supports NetworkPolicy. There are a number of CNI network plugins that support NetworkPolicy, including Calico, Cilium, Kube-router, Romana and Weave Net.
    enabled: false
  notification:        # Email Notification support for the legacy alerting system, should be enabled/disabled together with the above alerting option.
    enabled: true
  openpitrix:          # (2 Core, 3.6 G) Whether to install KubeSphere Application Store. It provides an application store for Helm-based applications, and offer application lifecycle management.
    enabled: false
  servicemesh:         # (0.3 Core, 300 MiB) Whether to install KubeSphere Service Mesh (Istio-based). It provides fine-grained traffic management, observability and tracing, and offer visualization for traffic topology.
    enabled: true
```



查看安装日志

```
kubectl logs -n kubesphere-system $(kubectl get pod -n kubesphere-system -l app=ks-install -o jsonpath='{.items[0].metadata.name}') -f
```

![image-20210505225326280](G:\note\image\image-20210505225326280.png)

###### 4 创建一个wordpress应用

创建好密钥 存储卷 然后创建应用选择需要的镜像就可以了



![image-20210505235007042](G:\note\image\image-20210505235007042.png)

###### 5 devops流水线构建

流程说明：

- **阶段一. Checkout SCM**: 拉取 GitHub 仓库代码
- **阶段二. Unit test**: 单元测试，如果测试通过了才继续下面的任务
- **阶段三. SonarQube analysis**：sonarQube 代码质量检测
- **阶段四. Build & push snapshot image**: 根据行为策略中所选择分支来构建镜像，并将 tag 为 `SNAPSHOT-$BRANCH_NAME-$BUILD_NUMBER`推送至 Harbor (其中 `$BUILD_NUMBER`为 pipeline 活动列表的运行序号)。
- **阶段五. Push latest image**: 将 master 分支打上 tag 为 latest，并推送至 DockerHub。
- **阶段六. Deploy to dev**: 将 master 分支部署到 Dev 环境，此阶段需要审核。
- **阶段七. Push with tag**: 生成 tag 并 release 到 GitHub，并推送到 DockerHub。
- **阶段八. Deploy to production**: 将发布的 tag 部署到 Production 环境。

1. 创建登录仓库需要的凭证

   github 凭证![image-20210506103448564](G:\note\image\image-20210506103448564.png)

   

​      docker hub凭证

​     kubeconfig凭证

  ```
kubeconfig 类型的凭证用于访问接入正在运行的 Kubernetes 集群，在流水线部署步骤将用到该凭证。注意，此处的 Content 将自动获取当前 KubeSphere 中的 kubeconfig 文件内容，若部署至当前 KubeSphere 中则无需修改，若部署至其它 Kubernetes 集群，则需要将其 kubeconfig 文件的内容粘贴至 Content 中。
  ```

​     ![image-20210506103620026](G:\note\image\image-20210506103620026.png)

1 sonarqube凭证 代码质量分析



这个kubesphere内部有组件

如果没有可以使用命令创建pod

```
kubectl create deployment sonarqube --image=sonarqube
kubectl expose deployment sonarqube --port=9000 --type=NodePort
```

使用命令查看暴露的端口号

```
kubectl get svc --all-namespaces
```

登录操作如下

https://v2-1.docs.kubesphere.io/docs/zh-CN/devops/sonarqube/

最后创建sonarqube凭证

![image-20210506113257938](G:\note\image\image-20210506113257938.png)

2 创建流水线项目

利用他的Dockerfile文件

```dockerfile
pipeline {
  agent {
    node {
      label 'maven'
    }
  }

    parameters {
        string(name:'TAG_NAME',defaultValue: '',description:'')
    }

    environment {
        DOCKER_CREDENTIAL_ID = 'dockerhub-id'
        GITHUB_CREDENTIAL_ID = 'github-id'
        KUBECONFIG_CREDENTIAL_ID = 'demo-kubeconfig'
        REGISTRY = 'docker.io'
        DOCKERHUB_NAMESPACE = 'chenzixue'
        GITHUB_ACCOUNT = 'chenzixue@163.com'
        APP_NAME = 'devops-java-sample'
        SONAR_CREDENTIAL_ID = 'sonar-qube'
    }

    stages {
      拉取代码
        stage ('checkout scm') { 
            steps {
                checkout(scm)
            }
        }
     测试代码
        stage ('unit test') {
            steps {
                container ('maven') {
                    sh 'mvn clean -gs `pwd`/configuration/settings.xml test'
                }
            }
        }
      分析代码
        stage('sonarqube analysis') {
          steps {
            container ('maven') {
              withCredentials([string(credentialsId: "$SONAR_CREDENTIAL_ID", variable: 'SONAR_TOKEN')]) {
                withSonarQubeEnv('sonar') {
                 sh "mvn sonar:sonar -gs `pwd`/configuration/settings.xml -Dsonar.branch=$BRANCH_NAME -Dsonar.login=$SONAR_TOKEN"
                }
              }
              timeout(time: 1, unit: 'HOURS') {
                waitForQualityGate abortPipeline: true
              }
            }
          }
        }
       打包镜像推送到仓库
        stage ('build & push') {
            steps {
                container ('maven') {
                    sh 'mvn -Dmaven.test.skip=true -gs `pwd`/configuration/settings.xml clean package'
                    sh 'docker build -f Dockerfile-online -t $REGISTRY/$DOCKERHUB_NAMESPACE/$APP_NAME:SNAPSHOT-$BRANCH_NAME-$BUILD_NUMBER .'
                    withCredentials([usernamePassword(passwordVariable : 'DOCKER_PASSWORD' ,usernameVariable : 'DOCKER_USERNAME' ,credentialsId : "$DOCKER_CREDENTIAL_ID" ,)]) {
                        sh 'docker push  $REGISTRY/$DOCKERHUB_NAMESPACE/$APP_NAME:SNAPSHOT-$BRANCH_NAME-$BUILD_NUMBER'
                    }
                }
            }
        }
        推送最新的镜像
        stage('push latest'){
           when{
             branch 'master'
           }
           steps{
                container ('maven') {
                  sh 'docker tag  $REGISTRY/$DOCKERHUB_NAMESPACE/$APP_NAME:SNAPSHOT-$BRANCH_NAME-$BUILD_NUMBER $REGISTRY/$DOCKERHUB_NAMESPACE/$APP_NAME:latest '
                  sh 'docker push  $REGISTRY/$DOCKERHUB_NAMESPACE/$APP_NAME:latest '
                }
           }
        }
        部署镜像=创建应用pod 并暴露端口
        stage('deploy to dev') {
          when{
            branch 'master'
          }
          steps { //部署到开发环境
            input(id: 'deploy-to-dev', message: 'deploy to dev?')    // deploy/dev-ol内的yaml文件
            kubernetesDeploy(configs: 'deploy/dev-ol/**', enableConfigSubstitution: true, kubeconfigId: "$KUBECONFIG_CREDENTIAL_ID")
          }
        }
        stage('push with tag'){
          when{
            expression{
              return params.TAG_NAME =~ /v.*/
            }
          }
          steps {
              container ('maven') {         //推送的代码仓库release版本
                input(id: 'release-image-with-tag', message: 'release image with tag?')
                  withCredentials([usernamePassword(credentialsId: "$GITHUB_CREDENTIAL_ID", passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                    sh 'git config --global user.email "kubesphere@yunify.com" '
                    sh 'git config --global user.name "kubesphere" '
                    sh 'git tag -a $TAG_NAME -m "$TAG_NAME" '
                    sh 'git push http://$GIT_USERNAME:$GIT_PASSWORD@github.com/$GITHUB_ACCOUNT/devops-java-sample.git --tags --ipv4'
                  }
                sh 'docker tag  $REGISTRY/$DOCKERHUB_NAMESPACE/$APP_NAME:SNAPSHOT-$BRANCH_NAME-$BUILD_NUMBER $REGISTRY/$DOCKERHUB_NAMESPACE/$APP_NAME:$TAG_NAME '
                sh 'docker push  $REGISTRY/$DOCKERHUB_NAMESPACE/$APP_NAME:$TAG_NAME '
          }
          }
        }
        stage('deploy to production') { //把上步镜像部署生产环境
          when{
            expression{
              return params.TAG_NAME =~ /v.*/
            }
          }
          steps {   ////部署到生产环境
            input(id: 'deploy-to-production', message: 'deploy to production?')
            kubernetesDeploy(configs: 'deploy/prod-ol/**', enableConfigSubstitution: true, kubeconfigId: "$KUBECONFIG_CREDENTIAL_ID")
          }
        }
    }
}
```

###### 6 kubesphere部署mysql集群

先准备好pvc 存储卷    mysql密钥   配置文件  然后创建mysql服务选用设置好的文件

第一 创建master配置文件

![image-20210507085135814](G:\note\image\image-20210507085135814.png)

创建mysql服务



![image-20210507084407019](G:\note\image\image-20210507084407019.png)

![image-20210507084452845](G:\note\image\image-20210507084452845.png)

![image-20210507084555120](G:\note\image\image-20210507084555120.png)

![image-20210507084732550](G:\note\image\image-20210507084732550.png)

![image-20210507085307477](G:\note\image\image-20210507085307477.png)

选创建的配置文件挂载



![image-20210507094400691](G:\note\image\image-20210507094400691.png)

进入master-mysql终端添加同步账户

```
mysql -uroot -p

GRANT REPLICATION SLAVE ON *.* to 'backup'@'%' identified by '123456';

查看 master 状态
show master status
```

进入slaver-mysql终端

```
mysql -uroot -p

设置主库连接
change master to
master_host='192.168.111.139',master_user='backup',master_password='123456',master_log_file='mysql-bin.000003',master_log_pos=0,master_port=31106;

启动从库同步
start slave;

查看从库状态
show slave status\G;
```

7 kubesphere部署redis集群

1 创建redis配置

![image-20210507110007787](G:\note\image\image-20210507110007787.png)

2 创建pvc

3 创建redis服务

![image-20210507110249507](G:\note\image\image-20210507110249507.png)

4 可以创建多个redis服务咋在配置他们的文件即可

####  kubespere部署自己的微服务

![image-20210507111325181](G:\note\image\image-20210507111325181.png)

可以给服务指定域名访问

![image-20210507112037442](G:\note\image\image-20210507112037442.png)

集成sonarqube

[将 SonarQube 集成到流水线 (kubesphere.com.cn)](https://kubesphere.com.cn/docs/devops-user-guide/how-to-integrate/sonarqube/)

在根目录pom文件中天剑

```xml
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>

    <!-- Sonar -->
    <!-- The destination file for the code coverage report has to be set to the same value
         in the parent pom and in each module pom. Then JaCoCo will add up information in
         the same report, so that, it will give the cross-module code coverage. -->
    <sonar.jacoco.reportPaths>${PWD}/./target/jacoco.exec</sonar.jacoco.reportPaths>
    <sonar.groovy.binaries>target/classes</sonar.groovy.binaries>
  </properties>
```



1 修改每个微服务添加prod环境的配置文件

![image-20210507111439288](G:\note\image\image-20210507111439288.png)

2 创建dockerfile文件

每个微服务内的文件 根目录下

```
FROM java:8  
EXPOSE 8080

VOLUME /tmp  //把数据挂载到tmp目录

ADD target/*.jar  /app.jar   //把git代码打包的jar包  复制到根目录下的/app.jar

RUN bash -c 'touch /app.jar'   //把项目根目录的下的jar包 复制到镜像内部/app.jar

ENTRYPOINT ["java","-jar","/app.jar","-Xms128m","-Xmx300m","--spring.profiles.active=prod"]
```

每个微服务deploy目录下创建生成pod的yaml文件

对外暴露端口30000开始

$PROJECT_NAME/deploy/**

```
apiVersion: apps/v1
kind: Deployment
metadata:
  labels:
    app: gulimall-cart     #app 相当于服务的id 也改为服务的名称
  name: gulimall-cart  #微服务的名称
  namespace: gulimall  #整个项目的名称
spec:
  progressDeadlineSeconds: 600
  replicas: 2  # 副本数  生成pod的数量
  selector: #选择器
    matchLabels:
      app: gulimall-cart
  strategy:
    rollingUpdate:
      maxSurge: 100%
      maxUnavailable: 50%
    type: RollingUpdate
  template:   #这次部署的模板内容具体参数
    metadata:
      labels:
        app: gulimall-cart
    spec:
      containers:
        - name: gulimall-cart #创建pod的名字
          image: $REGISTRY/$HARBOR_NAMESPACE/$APP_NAME:latest  /仓库地址/名称空间/微服务名称:版本号
          # readinessProbe:
            # httpGet:
              # path: /
              # port: 8080
            # timeoutSeconds: 10
            # failureThreshold: 30
            # periodSeconds: 5
          imagePullPolicy: IfNotPresent #镜像拉取策略
          ports:
            - containerPort: 8080
              protocol: TCP
          resources: #资源
            limits:
              cpu: 1000m
              memory: 600Mi
            requests:
              cpu: 100m
              memory: 100Mi
          terminationMessagePath: /dev/termination-log  #容器日志
          terminationMessagePolicy: File
      dnsPolicy: ClusterFirst
      restartPolicy: Always
      terminationGracePeriodSeconds: 30
---
apiVersion: v1
kind: Service
metadata:
  labels:
    app: gulimall-cart
  name: gulimall-cart
  namespace: gulimall
spec:
  ports:
    - name: http
      port: 8080          #k8s service分配给pod可以重复 app的值都是唯一的
      protocol: TCP
      targetPort: 8080   #镜像端口
      nodePort: 30001 #对外暴露的端口
  selector:
    app: gulimall-cart
  sessionAffinity: None
  type: NodePort
---  
```

3 jenkinsfile文件

可以直接使用界面

![image-20210507124033364](G:\note\image\image-20210507124033364.png)

```lua
pipeline {
  agent {
    node {
      label 'maven'
    }

  }
  stages {
    stage('拉取代码') {
      agent none
      steps {
        sh '$PROJECTI_VERSION'
        git(url: 'https://gitee.com/qq851088072/devops-java-sample.git', credentialsId: 'gitee', branch: 'master', changelog: true, poll: false)
        sh 'echo 正在构建 $PROJECT_NAME 版本号:$PROJECT_VERSION 仓库 $REGISTRY'
        container('maven'){
          sh 'mvn clean install -Dmaven.test.skip=true -gs `pwd`/settings.xml'
        }
      }
    }

    // stage('sonarqube analysis') {
    //   steps {
    //     container('maven') {
    //       withCredentials([string(credentialsId: "$SONAR_CREDENTIAL_ID", variable: 'SONAR_TOKEN')]) {
    //         withSonarQubeEnv('sonar') {
    //           sh 'echo 当前位置`pwd`'
    //           sh 'mvn clean install -Dmaven.test.skip=true'
    //           sh "mvn sonar:sonar -gs `pwd`/settings.xml -Dsonar.branch=$BRANCH_NAME -Dsonar.login=$SONAR_TOKEN"
    //         }
    //       }
    //       timeout(time: 1, unit: 'HOURS') {
    //             waitForQualityGate abortPipeline: true
    //       }
    //     }
    //   }
    // }
    stage ('构建镜像') {
        steps {
          // $$BUILD_NUMBER 构建的序号  系统自带的
            container ('maven') {
                sh 'mvn -Dmaven.test.skip=true -gs `pwd`/settings.xml clean package'
                sh 'cd $PROJECT_NAME &&  docker build -f $PROJECT_NAME/Dockerfile -t $REGISTRY/$DOCKERHUB_NAMESPACE/$PROJECT_NAME:SNAPSHOT-$BRANCH_NAME-$BUILD_NUMBER .'
                withCredentials([usernamePassword(passwordVariable : 'DOCKER_PASSWORD' ,usernameVariable : 'DOCKER_USERNAME' ,credentialsId : "$DOCKER_CREDENTIAL_ID" ,)]) {
                  sh 'docker tag  $REGISTRY/$DOCKERHUB_NAMESPACE/$PROJECT_NAME:SNAPSHOT-$BRANCH_NAME-$BUILD_NUMBER $REGISTRY/$DOCKERHUB_NAMESPACE/$PROJECT_NAME:latest '
                  sh 'docker push  $REGISTRY/$DOCKERHUB_NAMESPACE/$PROJECT_NAME:latest '
                }
            }
        }
    }
    stage('部署到集群') {
          when{
            branch 'master'
          }
          steps {
            input(id: 'deploy-to-dev-$PROJECT_NAME', message: "是否部署 $PROJECT_NAME 到集群?") //需要人工确认
            kubernetesDeploy(configs: '$PROJECT_NAME/deploy/*', enableConfigSubstitution: true, kubeconfigId: "$KUBECONFIG_CREDENTIAL_ID")
          }
        }
    stage('发布版本'){
          when{
            expression{
              return params.PROJECT_VERSION =~ /v.*/
            }
          }
          steps {
              container ('maven') {
                input(id: 'release-image-with-tag', message: "是否发布 $PROJECT_NAME:$PROJECT_VERSION 版本?")
                  withCredentials([usernamePassword(credentialsId: "$GITHUB_CREDENTIAL_ID", passwordVariable: 'GIT_PASSWORD', usernameVariable: 'GIT_USERNAME')]) {
                    sh 'git config --global user.email "kubesphere@yunify.com" '
                    sh 'git config --global user.name "kubesphere" '
                    sh 'git tag -a $TAG_NAME -m "$TAG_NAME" '
                    //github发布版本
                    sh 'git push http://$GIT_USERNAME:$GIT_PASSWORD@github.com/$GITHUB_ACCOUNT/devops-java-sample.git --tags --ipv4'
                  }
                  //dockerhub 发布版本
                sh 'docker tag  $REGISTRY/$DOCKERHUB_NAMESPACE/$APP_NAME:SNAPSHOT-$BRANCH_NAME-$BUILD_NUMBER $REGISTRY/$DOCKERHUB_NAMESPACE/$PROJECT_NAME:$PROJECT_VERSION '
                sh 'docker push  $REGISTRY/$DOCKERHUB_NAMESPACE/$PROJECT_NAME:$PROJECT_VERSION'
             }
          }
    }    
    
  }
  environment {
    DOCKER_CREDENTIAL_ID = 'dockerhub-id'
    GITHUB_CREDENTIAL_ID = 'github-id'
    KUBECONFIG_CREDENTIAL_ID = 'demo-kubeconfig'
    REGISTRY = 'docker.io'
    DOCKERHUB_NAMESPACE = 'guli'
    GITHUB_ACCOUNT = '851088072@qq.com'
    APP_NAME = 'devops-java-sample'
    SONAR_CREDENTIAL_ID = 'sonarqube-token'
    BRANCH_NAME = 'master'
  }
  parameters {
    string(name: 'PROJECT_NAME', defaultValue: '', description: '项目名称')
    string(name: 'PROJECT_VERSION', defaultValue: 'v0.0', description: '版本号')
  }
}
```

4 /configuration/settings.xml

```xml
<settings>
  <mirrors>
    <mirror>
      <id>aliyunmaven</id>
      <url>https://maven.aliyun.com/repository/public/</url>
      <mirrorOf>*</mirrorOf>
      <name>阿里云公共仓库</name>
    </mirror>
  </mirrors>
    <profile>     
      <id>JDK-1.8</id>       
      <activation>       
        <activeByDefault>true</activeByDefault>       
        <jdk>1.8</jdk>       
      </activation>       
      <properties>       
        <maven.compiler.source>1.8</maven.compiler.source>       
        <maven.compiler.target>1.8</maven.compiler.target>       
<maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>       
      </properties>       
    </profile>
</settings>
```

部署应用路由

可以根据域名访问服务

第一创建网关

![image-20210507162552138](G:\note\image\image-20210507162552138.png)

第一创建应用路由

![image-20210507162831110](G:\note\image\image-20210507162831110.png)

#### harbor镜像仓库

1 准备环境 安装好docker并启动docker

2 先安装docker-compose

```
sudo curl -L "https://github.com/docker/compose/releases/download/1.29.0/dockercompose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

给docker-compose添加执行权限
sudo chmod +x /usr/local/bin/docker-compose

查看docker-compose是否安装成功
docker-compose -version
```

3 下载harbor压缩包

```
https://github.com/goharbor/harbor/releases/download/v2.1.5/harbor-online-installer-v2.1.5.tgz

上传压缩包到linux，并解压
tar -xzf harbor-offline-installer-v1.9.2.tgz
mkdir /opt/harbor
mv harbor/* /opt/harbor
cd /opt/harbor


修改Harbor的配置
vi harbor.yml

hostname: 192.168.66.102
port: 85

安装Harbor
./prepare
./install.sh


启动Harbor
docker-compose up -d 启动
docker-compose stop 停止
docker-compose restart 重新启动
```

错误

```
安装Docker harbor报错：ERROR:root:Error: The protocol is https but attribute ssl_cert is not set
```

![img](G:\note\image\20201206221901487.png)

4 访问Harbor

```
192.168.111.156
```

修改docker配置添加仓库地址为信息列表

```
vi /etc/docker/daemon.json
{
"registry-mirrors": ["https://zydiol88.mirror.aliyuncs.com"],
  
}

```

推送镜像

```
打标签
tag 镜像名称 harbor仓库地址/镜像命名空间/镜像的名称:版本号
tag nginx 192.168.111.156/library/nginx:v1

docker login -u 用户名 -p 密码 192.168.111.156

docker push 192.168.111.156/library/nginx:v1
```





