#include <iostream>
#include <variant>
#include <string>
#include "tree.hpp"

int main(int argc, char const *argv[]) {
    std::variant<Tree<int>,Tree<double>,Tree<std::string>> tree;

    if (argc < 2) {
            std::cout << "!! Invalid number of arguments" << std::endl;
            return 1;
    }

    switch (argv[1][0]) {
        case 'i': tree.emplace<Tree<int>>(); break;
        case 'd': tree.emplace<Tree<double>>(); break;
        case 's': tree.emplace<Tree<std::string>>(); break;
        default:
            std::cout << "!! Invalid type" << std::endl;
            return 1;
    }

    std::visit([](auto& t) {
        std::string msg;
        while (true) {
            std::cout << ">> ";
            std::getline(std::cin, msg);
            if (msg == "exit") { break; }
            std::cout << t.interpret(msg) << std::endl;
        }
    }, tree);
    return 0;
}
