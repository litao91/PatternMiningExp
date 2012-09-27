function out4svm(cleaned_data) 
data_size = size(cleaned_data,2);
fp = fopen('svm_out.data', 'w');
for i = 1:data_size
    count = 1;
    fprintf(fp,'%d ', cleaned_data(i).label);
    seq_size = size(cleaned_data(i).seq,2);
    for j = 1:seq_size
        fprintf(fp, '%d ', cleaned_data(i).seq(j));
    end
    fprintf(fp,'\n');
end
fclose(fp);
end
