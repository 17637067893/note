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
   192.168.111.153 master1
   192.168.111.143 master2
   192.168.111.144 node1
   192.168.111.159 k8s-vip
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

   

2.  所有节点安装Docker/kubeadm/kubelet

   ```
   安装docker
   $ wget https://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo -O /etc/yum.repos.d/docker-ce.repo
   $ yum -y install docker-ce-18.06.1.ce-3.el7
   $ systemctl enable docker && systemctl start docker
   $ docker --version
   Docker version 18.06.1-ce, build e68fc7a
   
   
   修改仓库地址
   cat > /etc/docker/daemon.json << EOF
   {
     "registry-mirrors": ["https://b9pmyelo.mirror.aliyuncs.com"]
   }
   EOF
   
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
  --apiserver-advertise-address=192.168.111.153 \
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
        image: quay.io/coreos/flannel:v0.11.0-amd64
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
        image: quay.io/coreos/flannel:v0.11.0-amd64
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

5.  脑裂问题可能造成的原因