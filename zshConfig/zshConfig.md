# zsh을 사용하면서 적용한 config 목록

## git status 시 보여지는 파일들이 유니코드로 보여지는 것을 바꾸는 설정

- git config --global core.quotepath false

<br>

## vi 에디터를 사용할때 자동으로 줄번호가 나오게 하는 설정(vi 에디터 설정)

- vi ~/.vimrc 파일을 열고 아래의 내용을 입력한다

```bash
set nu
```

<br>

## 한글로 나오는 git 명령어 결과를 영어로 바꾸는 설정

1. 언어 설정을 확인한다

```
echo $LANG  // ko_KR.UTF-8 혹은 다른 언어 설정
```

2. 자신의 홈디렉토리에서 .zshrc 파일을 연다

```
vi ~/.zshrc
```

3. .zshrc 파일에 export LANG=en_US 를 추가하거나 git 명령어의 alias를 추가한다

```
export LANG=en_US    // 언어 설정 변경
alias git="LANG=en_US.UTF-8 git"     // git 명렁 alias 추가
```

4. 변경 사항 적용

```
source ~/.zshrc
```

<br>

## window terminal(zsh 사용)에서 git log 인코딩 문제로 한글이 깨져서 나옴

![](https://user-images.githubusercontent.com/23889744/148407233-8a533859-b6bb-4949-8417-5ae68d3dc7ff.png)

git 명령결과를 영어로 나오게 하는 설정을 적용할때 .zshrc에 LANG 값을 en_US로 고쳤는데 이렇게 하지않고 alias를 사용하는 방식으로 했더니 해결됨

```
export LANG=ko.KR_UTF-8    // 언어 설정 변경
alias git="LANG=en_US.UTF-8 git"     // git 명렁 alias 추가
```
