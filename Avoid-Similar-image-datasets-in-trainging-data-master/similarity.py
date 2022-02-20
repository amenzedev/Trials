from PIL import Image
import imagehash
from moviepy.editor import VideoFileClip,concatenate_videoclips, CompositeVideoClip
import cv2
import os
import shutil #for coping files

#get the folder name
path = os.path.abspath("similarity.py")
paths= path.split('/')
Folder_name=paths[-2]

#get all the images in the current folder
files = os.listdir('.')
for file in files: #removing non-image files
    if((".jpg" not in file) and (".jpeg" not in file)):
        files.remove(file)


#******** here change the level of similarity ************
#///////////////////////////////////////////////////////
cutoff = 5  # maximum bits that could be different between the hashes.
#///////////////////////////////////////////////////////////////






print("identifying similar images .......")
#save the similar images from the dataset to the below array
files_to_remove=[]
for i in range(len(files)-1):
    hash0 = imagehash.average_hash(Image.open(files[i]))
    for j in range(i+1,len(files)) :
        if files[j] not in files_to_remove:
            hash1 = imagehash.average_hash(Image.open(files[j]))
            if hash0 - hash1 < cutoff:
                files_to_remove.append(files[j])
                #print(files[j])


print("starting to copy images.....")
#remove the similar images from our file array
for file in files_to_remove:
    try:
        files.remove(file)
    except: print("no file exists")
print("files to be removed are :",len(files_to_remove))

#create a folder to add the unique datasets to that folder
try:
    os.mkdir(path.replace("similarity.py","")+"/"+Folder_name)
except:
    print("Folder exists")

#add the unique images to the created folder
for file in files:
    shutil.copy2(file,Folder_name+"/"+file)

#now you will have a unique image datasets in a folder named exactly like the parent folder
