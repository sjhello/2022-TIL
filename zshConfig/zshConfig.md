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
