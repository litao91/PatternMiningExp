% Clean the trojactory for one character, basically 
% change the continuous data to discrete data.
% The cleanning has three steps:
% 1. group the trojactory points in to groups of size 10 (consective pts grouped together)
% 2. get the mean of each groups, 
% 3. assign each group a class according to it's mean value
% The class assignment is specified in the getPtClass function
function data = cleanOneChar(Data, char_idx) 
ori_data = Data.mixout{char_idx};
x = ori_data(1,:);
y = ori_data(2,:);
[label_id ph] = whatis(Data,char_idx,0);
sample_size = size(ori_data,2);
sequence = [];
% I'm so lazy, so just ignore the remaining pts.
for i = 1:10:(sample_size-10)
    x_tmp = mean(x(i:i+10));
    y_tmp = mean(y(i:i+10));
    sequence(1,(i-1)/10+1) = getPtClass(x_tmp, y_tmp);
end
data = struct('seq',sequence,'label',label_id);
end

