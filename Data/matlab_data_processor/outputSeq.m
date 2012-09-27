function outputSeq(cleaned_data)
data_size = size(cleaned_data,2);
fp = fopen('seq.data', 'w');
for i = 1:data_size
    fprintf(fp, '%d ', cleaned_data(i).seq);
    fprintf(fp,'\n');
end
fclose(fp);
end
