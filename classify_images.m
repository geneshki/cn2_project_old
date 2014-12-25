pkg load general signal image

%A = imread("img1_small.jpg");
%B = imread("img2_small.jpg");
%A = imread("1.jpeg");
%B = imread("2.jpeg");
A = imread("light_same_direction_s2.jpg");
B = imread("light_same_direction_s.jpg");
C = imread("no_light_same_direction_s2.jpg");
D = imread("no_light_same_direction_s.jpg");
gA = rgb2gray(A);
gB = rgb2gray(B);
gC = rgb2gray(C);
gD = rgb2gray(D);

% check what is the mean of every gray image
mean2(gA)
mean2(gB)
mean2(gC)
mean2(gD)


% classify the image
% correlate the first image and the two images
cA = corr2(gA, gA)
cAB = corr2(gB,gA)
cC = corr2(gC, gA);
cCD =corr2(gC, gD);
cAC = corr2(gA, gC);
% get the difference between the two highest values in the correlation matrices
% as percentage of the auto-correlation max value
diff = max(cA) - max(cAB);
ratio = diff / max(cA)
plot(cA);
figure(2)
plot(cAB)
diff1 = max(max(cC)) - max(max(cCD));
ratio1 = diff1 / max(max(cC))
diff2 = max(max(cA)) - max(max(cAC));
ratio2 = diff2 / max(max(cA))

% check if it is under certain threshold
