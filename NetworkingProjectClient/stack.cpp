#include<iostream>
using namespace std;
struct node
{
	int data;
	node *next;
};

class SDD
{
public:
	void push();
	void pop();
	void print();
	SDD();
private:
	int size;
	node  *top;
	node *temp;
};

SDD::SDD()
{
	size = 0;
	top = NULL;
	temp = NULL;
}
void SDD::push()
{
	if (top == NULL)
	{
		top = new node;
		cin >> top->data;
		top->next = NULL;
		size++;
		return;
	}
		temp = new node;
		temp->next = top;
		cin >> temp->data;
		top = temp;
		size++;
}


void SDD::pop()
{
	if(top==NULL){
		cout<<"Stack is empty"<<endl;
		return;
		}
		node *del = top;
		cout<<top->data<<" ";
		top = top->next;
		delete del;
		del = NULL;
}
int main()
{
	SDD s;
	s.push();
	s.push();
	s.push();
	s.push();

	s.pop();
	s.pop();
	s.pop();
	s.pop();
	cout<<endl;
	s.pop();
	

	system("pause");
	return 0;
}