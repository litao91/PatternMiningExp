function outputSeq(cleaned_data,outfile)
data_size = size(cleaned_data,2);
fp = fopen(outfile, 'w');
for i = 1:data_size
    if(cleaned_data(i).label <= 8) 
        fprintf(fp, '%d ', cleaned_data(i).seq);
        fprintf(fp,'\n');
    end
end
fclose(fp);
end
