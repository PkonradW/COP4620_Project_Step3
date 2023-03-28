rm LittleLexer*
rm LittleParser*
antlr4 Little.g4
javac Little*.java

sh Micro.sh inputs/test1.micro
diff -B -b -s outputs/test1.out output.txt
sh Micro.sh inputs/test5.micro
diff -B -b -s outputs/test5.out output.txt
sh Micro.sh inputs/test6.micro
diff -B -b -s outputs/test6.out output.txt
sh Micro.sh inputs/test7.micro
diff -B -b -s outputs/test7.out output.txt
sh Micro.sh inputs/test8.micro
diff -B -b -s outputs/test8.out output.txt
sh Micro.sh inputs/test9.micro
diff -B -b -s outputs/test9.out output.txt
sh Micro.sh inputs/test11.micro
diff -B -b -s outputs/test11.out output.txt
sh Micro.sh inputs/test13.micro
diff -B -b -s outputs/test13.out output.txt
sh Micro.sh inputs/test14.micro
diff -B -b -s outputs/test14.out output.txt
sh Micro.sh inputs/test16.micro
diff -B -b -s outputs/test16.out output.txt
sh Micro.sh inputs/test18.micro
diff -B -b -s outputs/test18.out output.txt
sh Micro.sh inputs/test19.micro
diff -B -b -s outputs/test19.out output.txt
sh Micro.sh inputs/test20.micro
diff -B -b -s outputs/test20.out output.txt
sh Micro.sh inputs/test21.micro
diff -B -b -s outputs/test21.out output.txt