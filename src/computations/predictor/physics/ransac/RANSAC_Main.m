x = [1 2 3 4 5 6 7 8];
data = zeros(8,2);
data(:,1) = x;
data(:,2) = (x * 3 + 2)';

num = 2;
iter = 100;
threshDist = 1;
inlierRatio = 1;

[bestParameter1,bestParameter2] = RANSAC(data',num,iter,threshDist,inlierRatio)

data(4,2) = 22;

[bestParameter1,bestParameter2] = RANSAC(data',num,iter,1,0.1)