% clean all the sequences one by one,
% by calling the cleanOneChar function
function data = cleanData(ori_data)
data_size = size(ori_data.mixout,2);
for i = 1:data_size
    data(i) = cleanOneChar(ori_data,i);
end
end
