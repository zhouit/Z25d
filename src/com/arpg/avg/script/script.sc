set heroName = 东方上人
set toHero = 周浩成
set heroImage = resource/image/role/assassin_face.png
set toImage = resource/image/role/rogue_face.png

cg  del
cg  #{toImage}
mes #{heroName},听说你武功天下第一,敢和我比一下吗?

cg del #{toImage}
mes 黄毛小子,竟敢在此口出狂言!!!

cg  #{toImage}
mes 谁...谁...谁在说话???

cg del #{toImage}
cg  #{heroImage} 400
mes 哼哼,无名小辈,我懒得跟你比....

cg del #{heroImage}
cg  #{toImage}
mes 我去年买了个表,&现在有个问题要请教你下，那就是——

mes #{toHero}长的帅不帅？
   in 
	A.根本不能用人类的相貌加以形容。
	B.你不知他人送绰号“三重刘德华”吗？
	C.“帅”字和他绝缘很多年。 
   out
	   
if SELECT==0
   mes 你很诚实哦~
elseif SELECT==1
   mes 说谎是做贼的开始……
elseif SELECT==2
   mes 这个嘛，嗯，确实，但不够贴切……
endif 
